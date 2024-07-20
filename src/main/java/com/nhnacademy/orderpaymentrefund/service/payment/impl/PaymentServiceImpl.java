package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.client.client.ClientServiceFeignClient;
import com.nhnacademy.orderpaymentrefund.client.payment.TossPaymentsClient;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverter;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.client.ClientUpdateGradeRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.coupon.PaymentCompletedCouponResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.TossApprovePaymentRequest;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.point.PointUsagePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.product.CartCheckoutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.product.InventoryDecreaseRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final String ID_HEADER = "X-User-Id";

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final TossPaymentsClient tossPaymentsClient;
    private final String tossSecretKey;
    private final ClientServiceFeignClient clientServiceFeignClient;

    private final RabbitTemplate rabbitTemplate;

    private final ProductOrderDetailConverter productOrderDetailConverter;
    private final ProductOrderDetailOptionConverter productOrderDetailOptionConverter;

    @Value("${rabbit.cart.checkout.exchange.name}")
    private String cartCheckoutExchangeName;
    @Value("${rabbit.cart.checkout.routing.key}")
    private String cartCheckoutRoutingKey;

    @Value("${rabbit.inventory.decrease.exchange.name}")
    private String inventoryDecreaseExchangeName;
    @Value("${rabbit.inventory.decrease.routing.key}")
    private String inventoryDecreaseRoutingKey;

    @Value("${rabbit.inventory.increase.exchange.name}")
    private String inventoryIncreaseExchangeName;
    @Value("${rabbit.inventory.increase.routing.key}")
    private String inventoryIncreaseRoutingKey;

    @Value("${rabbit.use.point.exchange.name}")
    private String pointUseExchangeName;
    @Value("${rabbit.use.point.routing.key}")
    private String pointUseRoutingKey;

    @Value("${rabbit.use.coupon.exchange.name}")
    private String couponUseExchangeName;
    @Value("${rabbit.use.coupon.roting.key}")
    private String couponUseRoutingKey;

    // Order Enum Type -> String, 배송 상태 -> tinyInt
    @Override
    public void savePayment(HttpHeaders headers, TossPaymentsResponseDto tossPaymentsResponseDto,
        HttpServletResponse response) {

        Long clientId = getClientId(headers);

        if (clientId != null) {
            Object data = redisTemplate.opsForHash()
                .get("order", tossPaymentsResponseDto.getOrderId());
            ClientOrderCreateForm clientOrderCreateForm = objectMapper.convertValue(data,
                ClientOrderCreateForm.class);

            // order 저장
            Order order = Order.clientOrderBuilder()
                .clientId(clientId)
                .couponId(clientOrderCreateForm.getCouponId())
                .tossOrderId(clientOrderCreateForm.getTossOrderId())
                .productTotalAmount(clientOrderCreateForm.getProductTotalAmount())
                .shippingFee(clientOrderCreateForm.getShippingFee())
                .designatedDeliveryDate(clientOrderCreateForm.getDesignatedDeliveryDate())
                .phoneNumber(clientOrderCreateForm.getPhoneNumber())
                .deliveryAddress(clientOrderCreateForm.getDeliveryAddress())
                .discountAmountByPoint(clientOrderCreateForm.getUsedPointDiscountAmount())
                .discountAmountByCoupon(clientOrderCreateForm.getCouponDiscountAmount())
                .accumulatedPoint(clientOrderCreateForm.getAccumulatePoint())
                .build();
            orderRepository.save(order);

            // OrderProductDetail + OrderProductDetailOption 생성 및 저장
            clientOrderCreateForm.getOrderDetailDtoItemList().forEach((item) -> {
                ProductOrderDetail productOrderDetail = productOrderDetailConverter.dtoToEntity(
                    item, order);
                productOrderDetailRepository.save(productOrderDetail);
                if (item.getUsePackaging()) {
                    ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionConverter.dtoToEntity(
                        item, productOrderDetail);
                    productOrderDetailOptionRepository.save(productOrderDetailOption);
                }
            });

            // payment 저장
            Payment payment = Payment.builder()
                .order(order)
                .payAmount(tossPaymentsResponseDto.getTotalAmount())
                .paymentMethodName(tossPaymentsResponseDto.getMethod())
                .tossPaymentKey(tossPaymentsResponseDto.getPaymentKey())
                .build();

            paymentRepository.save(payment);

            // 회원 등급변경, 포인트 사용, 쿠폰 사용, 재고처리, 장바구니 비우기
            ClientUpdateGradeRequestDto clientUpdateGradeRequestDto = ClientUpdateGradeRequestDto.builder()
                .clientId(clientId)
                .payment(getPaymentRecordOfClient(clientId).getPaymentGradeValue())
                .build();
            clientServiceFeignClient.updateClientGrade(clientUpdateGradeRequestDto);

            // 장바구니 비우기 위한 요청 dto
            CartCheckoutRequestDto cartCheckoutRequestDto = CartCheckoutRequestDto.builder()
                .clientId(clientId).build();

            Map<Long, Long> decreaseInfo = new HashMap<>();
            clientOrderCreateForm.getOrderDetailDtoItemList()
                .forEach(
                    orderDetail -> {
                        decreaseInfo.put(orderDetail.getProductId(), orderDetail.getQuantity());
                        if (orderDetail.getUsePackaging()) {
                            decreaseInfo.put(orderDetail.getOptionProductId(),
                                orderDetail.getOptionQuantity());
                        }
                    }
                );

            InventoryDecreaseRequestDto inventoryDecreaseRequestDto = InventoryDecreaseRequestDto.builder()
                .orderId(order.getOrderId()).decreaseInfo(decreaseInfo).build();

            // 상품 및 옵션 상품 재고처리
            rabbitTemplate.convertAndSend(inventoryDecreaseExchangeName,
                inventoryDecreaseRoutingKey, inventoryDecreaseRequestDto);

            // 구입 상품 장바구니 삭제
            rabbitTemplate.convertAndSend(cartCheckoutExchangeName, cartCheckoutRoutingKey,
                cartCheckoutRequestDto);

            // 포인트 사용
            Long usedPointDiscountAmount = clientOrderCreateForm.getUsedPointDiscountAmount();
            PointUsagePaymentRequestDto pointUsagePaymentRequestDto = PointUsagePaymentRequestDto.builder()
                .pointUsageAmount(usedPointDiscountAmount)
                .clientId(clientId)
                .build();

            if (usedPointDiscountAmount != null && usedPointDiscountAmount > 0) {
                rabbitTemplate.convertAndSend(pointUseExchangeName, pointUseRoutingKey,
                    pointUsagePaymentRequestDto);
            }

            // 쿠폰 사용
            Long usedCouponDiscountAmount = clientOrderCreateForm.getCouponDiscountAmount();
            PaymentCompletedCouponResponseDto paymentCompletedCouponResponseDto = PaymentCompletedCouponResponseDto.builder()
                .couponId(clientOrderCreateForm.getCouponId())
                .build();

            if (usedCouponDiscountAmount != null && usedCouponDiscountAmount > 0) {
                rabbitTemplate.convertAndSend(couponUseExchangeName, couponUseRoutingKey,
                    paymentCompletedCouponResponseDto);
            }

            redisTemplate.opsForHash().delete("order", clientOrderCreateForm.getTossOrderId());

        } else {

            Object data = redisTemplate.opsForHash()
                .get("order", tossPaymentsResponseDto.getOrderId());
            NonClientOrderForm nonClientOrderForm = objectMapper.convertValue(data,
                NonClientOrderForm.class);

            // order 저장
            Order order = Order.nonClientOrderBuilder()
                .tossOrderId(nonClientOrderForm.getTossOrderId())
                .productTotalAmount(nonClientOrderForm.getProductTotalAmount())
                .shippingFee(nonClientOrderForm.getShippingFee())
                .designatedDeliveryDate(nonClientOrderForm.getDesignatedDeliveryDate())
                .phoneNumber(nonClientOrderForm.getPhoneNumber())
                .deliveryAddress(nonClientOrderForm.getDeliveryAddress())
                .nonClientOrdererName(nonClientOrderForm.getOrderedPersonName())
                .nonClientOrdererEmail(nonClientOrderForm.getEmail())
                .nonClientOrderPassword(nonClientOrderForm.getOrderPassword())
                .build();
            orderRepository.save(order);

            // OrderProductDetail + OrderProductDetailOption 생성 및 저장
            nonClientOrderForm.getOrderDetailDtoItemList().forEach((item) -> {
                ProductOrderDetail productOrderDetail = productOrderDetailConverter.dtoToEntity(
                    item, order);
                productOrderDetailRepository.save(productOrderDetail);
                if (item.getUsePackaging()) {
                    ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionConverter.dtoToEntity(
                        item, productOrderDetail);
                    productOrderDetailOptionRepository.save(productOrderDetailOption);
                }
            });

            // payment 저장
            Payment payment = Payment.builder()
                .order(order)
                .payAmount(tossPaymentsResponseDto.getTotalAmount())
                .paymentMethodName(tossPaymentsResponseDto.getMethod())
                .tossPaymentKey(tossPaymentsResponseDto.getPaymentKey())
                .build();

            paymentRepository.save(payment);

            redisTemplate.opsForHash().delete("order", nonClientOrderForm.getTossOrderId());

        }

    }

    @Override
    public PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId) {
        Long totalOptionPriceForLastThreeMonth = orderRepository.getTotalOptionPriceForLastThreeMonths(
            clientId, LocalDateTime.now().minusDays(90L));
        if (totalOptionPriceForLastThreeMonth == null) {
            totalOptionPriceForLastThreeMonth = 0L;
        }
        log.error("totalOptionPriceForLastThreeMonth: {}", totalOptionPriceForLastThreeMonth);

        Long sumFinalAmountForCompletedOrders = orderRepository.sumFinalAmountForCompletedOrders(
            clientId, LocalDateTime.now().minusDays(90L));
        log.error("sumFinalAmountForCompletedOrders: {}", sumFinalAmountForCompletedOrders);

        if (sumFinalAmountForCompletedOrders == null) {
            sumFinalAmountForCompletedOrders = 0L;
        }
        return PaymentGradeResponseDto.builder()
            .paymentGradeValue(sumFinalAmountForCompletedOrders - totalOptionPriceForLastThreeMonth)
            .build();
    }

    @Override
    public TossPaymentsResponseDto approvePayment(
        TossApprovePaymentRequest tossApprovePaymentRequest) throws ParseException {

        // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(tossSecretKey.getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 승인 요청을 보내면서 + 응답을 받아 옴.
        String tossPaymentsApproveResponseString = tossPaymentsClient.approvePayment(
            tossApprovePaymentRequest, authorizations);

        // 다시 한 번 JSONObject 로 변환한다.
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(
            tossPaymentsApproveResponseString);

        String orderName = jsonObject.get("orderName").toString();
        String totalAmount = jsonObject.get("totalAmount").toString();
        String method = jsonObject.get("method").toString();
        String cardNumber = null;
        String accountNumber = null;
        String bank = null;
        String customerMobilePhone = null;

        if (method.equals("카드")) {
            cardNumber = ((JSONObject) jsonObject.get("card")).get("number").toString();
        } else if (method.equals("가상계좌")) {
            accountNumber = ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber")
                .toString();
        } else if (method.equals("계좌이체")) {
            bank = ((JSONObject) jsonObject.get("transfer")).get("bank").toString();
        } else if (method.equals("휴대폰")) {
            customerMobilePhone = ((JSONObject) jsonObject.get("mobilePhone")).get(
                "customerMobilePhone").toString();
        } else if (method.equals("간편결제")) {
            method =
                method + "-" + ((JSONObject) jsonObject.get("easyPay")).get("provider").toString();
        }

        return TossPaymentsResponseDto.builder()
            .orderName(orderName)
            .totalAmount(Long.parseLong(totalAmount))
            .method(method)
            .paymentKey(tossApprovePaymentRequest.getPaymentKey())
            .cardNumber(cardNumber)
            .accountNumber(accountNumber)
            .bank(bank)
            .customerMobilePhone(customerMobilePhone)
            .orderId(tossApprovePaymentRequest.getOrderId())
            .build();
    }

    private Long getClientId(HttpHeaders headers) {
        if (headers.get(ID_HEADER) == null) {
            return null;
        }
        return Long.parseLong(headers.getFirst(ID_HEADER));
    }

    @Override
    public PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(
        String tossOrderId) {

        Order order = orderRepository.getOrderByTossOrderId(tossOrderId).orElseThrow(
            OrderNotFoundException::new);

        Payment payment = paymentRepository.findByOrder_OrderId(order.getOrderId())
            .orElseThrow(() -> new PaymentNotFoundException(tossOrderId));

        List<ProductOrderDetail> productOrderDetailList = productOrderDetailRepository.findAllByOrder(
            order);

        PostProcessRequiredPaymentResponseDto postProcessRequiredPaymentResponseDto = PostProcessRequiredPaymentResponseDto.builder()
            .clientId(order.getClientId())
            .amount(payment.getPayAmount())
            .paymentMethodName(payment.getPaymentMethodName())
            .build();

        for (ProductOrderDetail productOrderDetail : productOrderDetailList) {
            postProcessRequiredPaymentResponseDto.addProductIdList(
                productOrderDetail.getProductId());
        }

        return postProcessRequiredPaymentResponseDto;
    }
}
package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverter;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.ClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.NonClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.domain.payment.PaymentMethodType;
import com.nhnacademy.orderpaymentrefund.dto.coupon.PaymentCompletedCouponResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.message.PointUsagePaymentMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.product.CartCheckoutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.product.InventoryDecreaseRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.ClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.NonClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentMethodTypeRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ClientOrderRepository clientOrderRepository;
    private final NonClientOrderRepository nonClientOrderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PaymentMethodTypeRepository paymentMethodTypeRepository;

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

    @Value("${rabbit.use.point.exchange.name}")
    private String pointUseExchangeName;
    @Value("${rabbit.use.point.routing.key}")
    private String pointUseRoutingKey;

    @Value("${rabbit.use.coupon.exchange.name}")
    private String couponUseExchangeName;
    @Value("${rabbit.use.coupon.roting.key}")
    private String couponUseRoutingKey;

    private static final String ORDER = "order";

    @Override
    public void savePayment(HttpHeaders headers, PaymentsResponseDto paymentsResponseDto) {

        Long clientId = getClientId(headers);

        Object data = redisTemplate.opsForHash()
            .get(ORDER, paymentsResponseDto.getOrderCode());

        if (clientId != null) {
            processClientOrderAndPayment(clientId, data, paymentsResponseDto);
        } else {
            processNonClientOrderAndPayment(data, paymentsResponseDto);
        }

    }

    // 회원 주문 및 결제 생성 및 후처리 프로세스
    private void processClientOrderAndPayment(Long clientId, Object data,
        PaymentsResponseDto paymentsResponseDto) {

        ClientOrderCreateForm clientOrderCreateForm = objectMapper.convertValue(data,
            ClientOrderCreateForm.class);

        // order 저장
        Order order = saveClientOrderEntity(clientId, clientOrderCreateForm);

        // OrderProductDetail + OrderProductDetailOption 저장
        saveOrderProductDetailAndOrderProductDetailOption(order, clientOrderCreateForm.getOrderDetailDtoItemList());

        // payment 저장
        savePaymentEntity(order, paymentsResponseDto);

        // 후처리 - 회원 장바구니 비우기
        postProcessingClientCartCheckout(clientId, clientOrderCreateForm);

        // 후처리 - 포인트 사용
        postProcessingUsingPoint(clientId, clientOrderCreateForm);

        // 후처리 - 쿠폰 사용
        postProcessingUsingCoupon(clientOrderCreateForm);

        // 후처리 - 재고감소
        postProcessingInventoryDecrease(order.getOrderId(), clientOrderCreateForm.getOrderDetailDtoItemList());

        redisTemplate.opsForHash().delete(ORDER, clientOrderCreateForm.getOrderCode());

    }

    // 비회원 주문 및 결제 생성 및 후처리 프로세스
    private void processNonClientOrderAndPayment(Object data,
        PaymentsResponseDto paymentsResponseDto) {

        NonClientOrderForm nonClientOrderForm = objectMapper.convertValue(data,
            NonClientOrderForm.class);

        // order 저장
        Order order = saveNonClientOrderEntity(nonClientOrderForm);

        // OrderProductDetail + OrderProductDetailOption 생성 및 저장
        saveOrderProductDetailAndOrderProductDetailOption(order, nonClientOrderForm.getOrderDetailDtoItemList());

        // payment 저장
        savePaymentEntity(order, paymentsResponseDto);

        // 후처리 - 재고감소
        postProcessingInventoryDecrease(order.getOrderId(), nonClientOrderForm.getOrderDetailDtoItemList());

        redisTemplate.opsForHash().delete(ORDER, nonClientOrderForm.getOrderCode());

    }

    // 후처리 - 쿠폰 사용
    private void postProcessingUsingCoupon(ClientOrderCreateForm clientOrderCreateForm) {
        // 쿠폰 사용
        Long usedCouponDiscountAmount = clientOrderCreateForm.getCouponDiscountAmount();
        PaymentCompletedCouponResponseDto paymentCompletedCouponResponseDto = PaymentCompletedCouponResponseDto.builder()
            .couponId(clientOrderCreateForm.getCouponId())
            .build();

        if (usedCouponDiscountAmount != null && usedCouponDiscountAmount > 0) {
            rabbitTemplate.convertAndSend(couponUseExchangeName, couponUseRoutingKey,
                paymentCompletedCouponResponseDto);
        }
    }

    // 후처리 - 포인트 사용
    private void postProcessingUsingPoint(Long clientId,
        ClientOrderCreateForm clientOrderCreateForm) {
        Long usedPointDiscountAmount = clientOrderCreateForm.getUsedPointDiscountAmount();
        PointUsagePaymentMessageDto pointUsagePaymentRequestDto = PointUsagePaymentMessageDto.builder()
            .pointUsagePayment(usedPointDiscountAmount)
            .clientId(clientId)
            .build();

        if (usedPointDiscountAmount != null && usedPointDiscountAmount > 0) {
            rabbitTemplate.convertAndSend(pointUseExchangeName, pointUseRoutingKey,
                pointUsagePaymentRequestDto);
        }

    }

    private void postProcessingInventoryDecrease(Long orderId, List<OrderDetailDtoItem> orderDetailDtoItemList) {

        Map<Long, Long> decreaseInfo = new HashMap<>();

        for(OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList){
            decreaseInfo.put(orderDetailDtoItem.getProductId(), orderDetailDtoItem.getQuantity());
            if (orderDetailDtoItem.getUsePackaging()) {
                decreaseInfo.put(orderDetailDtoItem.getOptionProductId(), orderDetailDtoItem.getOptionQuantity());
            }
        }

        InventoryDecreaseRequestDto inventoryDecreaseRequestDto = InventoryDecreaseRequestDto.builder()
            .orderId(orderId).decreaseInfo(decreaseInfo).build();

        rabbitTemplate.convertAndSend(inventoryDecreaseExchangeName,
            inventoryDecreaseRoutingKey, inventoryDecreaseRequestDto);

    }


    // 후처리 - 회원 장바구니 데이터베이스에서 비우기
    private void postProcessingClientCartCheckout(Long clientId,
        ClientOrderCreateForm clientOrderCreateForm) {

        CartCheckoutRequestDto cartCheckoutRequestDto = CartCheckoutRequestDto.builder()
            .clientId(clientId).build();

        clientOrderCreateForm.getOrderDetailDtoItemList()
            .forEach(
                orderDetail ->
                    cartCheckoutRequestDto.addProductId(orderDetail.getProductId())
            );

        // 구입 상품 장바구니 삭제 - 큐로 보내기
        rabbitTemplate.convertAndSend(cartCheckoutExchangeName, cartCheckoutRoutingKey,
            cartCheckoutRequestDto);

    }

    private Order saveClientOrderEntity(Long clientId,
        ClientOrderCreateForm clientOrderCreateForm) {

        // order 생성 및 저장
        Order order = Order.builder()
            .orderCode(clientOrderCreateForm.getOrderCode())
            .productTotalAmount(clientOrderCreateForm.getProductTotalAmount())
            .shippingFee(clientOrderCreateForm.getShippingFee())
            .designatedDeliveryDate(clientOrderCreateForm.getDesignatedDeliveryDate())
            .phoneNumber(clientOrderCreateForm.getPhoneNumber())
            .deliveryAddress(clientOrderCreateForm.getDeliveryAddress())
            .build();

        orderRepository.save(order);

        // client order 생성 및 저장
        ClientOrder clientOrder = ClientOrder.builder()
            .clientId(clientId)
            .couponId(clientOrderCreateForm.getCouponId())
            .discountAmountByPoint(
                clientOrderCreateForm.getUsedPointDiscountAmount() == null ? 0 : clientOrderCreateForm.getUsedPointDiscountAmount())
            .discountAmountByCoupon(
                clientOrderCreateForm.getCouponDiscountAmount() == null ? 0 : clientOrderCreateForm.getCouponDiscountAmount())
            .accumulatedPoint(
                clientOrderCreateForm.getAccumulatePoint() == null ? 0 : clientOrderCreateForm.getAccumulatePoint())
            .order(order)
            .build();

        clientOrderRepository.save(clientOrder);

        return order;

    }

    private Order saveNonClientOrderEntity(
        NonClientOrderForm nonClientOrderForm) {

        Order order = Order.builder()
            .orderCode(nonClientOrderForm.getOrderCode())
            .productTotalAmount(nonClientOrderForm.getProductTotalAmount())
            .designatedDeliveryDate(nonClientOrderForm.getDesignatedDeliveryDate())
            .phoneNumber(nonClientOrderForm.getPhoneNumber())
            .deliveryAddress(nonClientOrderForm.getDeliveryAddress())
            .shippingFee(nonClientOrderForm.getShippingFee())
            .build();

        orderRepository.save(order);

        // nonclient order 생성 및 저장
        NonClientOrder nonClientOrder = NonClientOrder.builder()
            .nonClientOrderPassword(nonClientOrderForm.getOrderPassword())
            .nonClientOrdererEmail(nonClientOrderForm.getEmail())
            .nonClientOrdererName(nonClientOrderForm.getOrderedPersonName())
            .order(order)
            .build();

        nonClientOrderRepository.save(nonClientOrder);

        return order;
    }

    private void savePaymentEntity(Order order, PaymentsResponseDto paymentsResponseDto) {
        PaymentMethodType paymentMethodType = paymentMethodTypeRepository.findByPaymentMethodTypeNameEquals(paymentsResponseDto.getMethodType());
        Payment payment = Payment.builder()
            .order(order)
            .paymentMethodType(paymentMethodType)
            .payAmount(paymentsResponseDto.getTotalAmount())
            .paymentMethodName(paymentsResponseDto.getMethod())
            .tossPaymentKey(paymentsResponseDto.getPaymentKey())
            .build();
        paymentRepository.save(payment);
    }

    private void saveOrderProductDetailAndOrderProductDetailOption(Order order,
        List<OrderDetailDtoItem> orderDetailDtoItemList) {

        for(OrderDetailDtoItem item : orderDetailDtoItemList){
            ProductOrderDetail productOrderDetail = productOrderDetailConverter.dtoToEntity(
                item, order);
            productOrderDetailRepository.save(productOrderDetail);
            if (Boolean.TRUE.equals(item.getUsePackaging())) {
                ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionConverter.dtoToEntity(
                    item, productOrderDetail);
                productOrderDetailOptionRepository.save(productOrderDetailOption);
            }
        }
    }

    @Override
    public PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId) {
        Long totalOptionPriceForLastThreeMonth = clientOrderRepository.getTotalOptionPriceForLastThreeMonths(
            clientId, LocalDateTime.now().minusDays(90L));
        if (totalOptionPriceForLastThreeMonth == null) {
            totalOptionPriceForLastThreeMonth = 0L;
        }
        log.error("totalOptionPriceForLastThreeMonth: {}", totalOptionPriceForLastThreeMonth);

        Long sumFinalAmountForCompletedOrders = clientOrderRepository.sumFinalAmountForCompletedOrders(
            clientId, LocalDateTime.now().minusDays(90L));
        log.error("sumFinalAmountForCompletedOrders: {}", sumFinalAmountForCompletedOrders);

        if (sumFinalAmountForCompletedOrders == null) {
            sumFinalAmountForCompletedOrders = 0L;
        }
        return PaymentGradeResponseDto.builder()
            .paymentGradeValue(sumFinalAmountForCompletedOrders - totalOptionPriceForLastThreeMonth)
            .build();
    }

    private Long getClientId(HttpHeaders headers) {
        if (headers.get(ID_HEADER) == null) {
            return null;
        }
        return Long.parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
    }

    @Override
    public PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(
        HttpHeaders headers, String orderCode) {

        Order order = orderRepository.getOrderByOrderCode(orderCode).orElseThrow(
            OrderNotFoundException::new);

        Long clientId = getClientId(headers);

        Payment payment = paymentRepository.findByOrder_OrderId(order.getOrderId())
            .orElseThrow(() -> new PaymentNotFoundException(orderCode));

        List<ProductOrderDetail> productOrderDetailList = productOrderDetailRepository.findAllByOrder_OrderId(
            order.getOrderId());

        PostProcessRequiredPaymentResponseDto postProcessRequiredPaymentResponseDto = PostProcessRequiredPaymentResponseDto.builder()
            .orderId(order.getOrderId())
            .clientId(clientId)
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
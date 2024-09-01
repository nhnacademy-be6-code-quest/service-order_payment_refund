package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.context.PaymentContext;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.domain.payment.PaymentMethodType;
import com.nhnacademy.orderpaymentrefund.dto.coupon.PaymentCompletedCouponResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.message.PointUsagePaymentMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderForm;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentSaveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentMethodResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.SuccessPaymentOrderInfo;
import com.nhnacademy.orderpaymentrefund.dto.product.CartCheckoutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.product.InventoryDecreaseRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.ClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentMethodTypeRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import com.nhnacademy.orderpaymentrefund.util.OrderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ClientHeaderContext clientHeaderContext;
    private final OrderUtil orderUtil;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PaymentMethodTypeRepository paymentMethodTypeRepository;

    private final RabbitTemplate rabbitTemplate;

    private final OrderService orderService;
    private final ClientOrderService clientOrderService;
    private final NonClientOrderService nonClientOrderService;
    private final PaymentStrategyService paymentStrategyService;

    private final PaymentContext paymentContext;

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
    public Payment savePayment(Order order, PaymentSaveRequestDto paymentSaveRequestDto) {

        PaymentMethodType paymentMethodType = paymentMethodTypeRepository.findByPaymentMethodTypeNameEquals(
                paymentSaveRequestDto.getPaymentMethodTypeName());

        Payment payment = Payment.builder()
                .order(order)
                .paymentMethodType(paymentMethodType)
                .payAmount(paymentSaveRequestDto.getTotalPayAmount())
                .paymentMethodName(paymentSaveRequestDto.getPaymentMethodName())
                .paymentKey(paymentSaveRequestDto.getPaymentKey())
                .build();

        return paymentRepository.save(payment);

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

    @Override
    public PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(
        HttpHeaders headers, String orderCode) {

        Order order = orderRepository.getOrderByOrderCode(orderCode).orElseThrow(
            OrderNotFoundException::new);

        Long clientId = clientHeaderContext.getClientId();

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

    @Override
    public List<PaymentMethodResponseDto> getPaymentMethodList() {

        List<PaymentMethodResponseDto> responseDtoList = new ArrayList<>();

        List<PaymentMethodType> paymentMethodTypeList = paymentMethodTypeRepository.findAll();

        for (PaymentMethodType paymentMethodType : paymentMethodTypeList) {
            PaymentMethodResponseDto dto = PaymentMethodResponseDto.builder()
                .paymentMethodTypeId(paymentMethodType.getPaymentMethodTypeId())
                .paymentMethodTypeName(paymentMethodType.getPaymentMethodTypeName())
                .build();

            responseDtoList.add(dto);
        }

        return responseDtoList;
    }

    @Override
    public PaymentApproveResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto) {

        // 승인 및 응답
        PaymentApproveResponseDto paymentApproveResponseDto = paymentStrategyService.approvePayment(approvePaymentRequestDto);

        // 주문 저장
        Order order = saveOrder(approvePaymentRequestDto.getOrderCode());

        // 결제 저장
        PaymentSaveRequestDto paymentSaveRequestDto = getPaymentSaveRequestDto(approvePaymentRequestDto);
        Payment payment = savePayment(order, paymentSaveRequestDto);

        SuccessPaymentOrderInfo successPaymentOrderInfo = paymentStrategyService.getSuccessPaymentOrderInfo(approvePaymentRequestDto.getPgName(), paymentApproveResponseDto, order, payment);
        paymentApproveResponseDto.setSuccessPaymentOrderInfo(successPaymentOrderInfo);

        // 결제승인 후처리
        OrderForm orderForm = orderUtil.getOrderForm(approvePaymentRequestDto.getOrderCode());
        if(clientHeaderContext.isClient() && orderForm instanceof ClientOrderForm clientOrderForm){
            postProcessingClientCartCheckout(clientHeaderContext.getClientId(), clientOrderForm);
            postProcessingUsingPoint(clientHeaderContext.getClientId(), clientOrderForm);
            postProcessingUsingCoupon(clientOrderForm);
        }

        postProcessingInventoryDecrease(order.getOrderId(), orderForm.getOrderItemList());
        redisTemplate.opsForHash().delete(ORDER, orderForm.getOrderCode());

        return paymentApproveResponseDto;
    }

    private Order saveOrder(String orderCode){
        OrderForm orderForm = orderUtil.getOrderForm(orderCode);
        Order order = orderService.saveOrder(orderForm);
        if(clientHeaderContext.isClient() && orderForm instanceof ClientOrderForm clientOrderForm){
            clientOrderService.saveClientOrder(order, clientOrderForm);
        }
        else if(!clientHeaderContext.isClient() && orderForm instanceof NonClientOrderForm nonClientOrderForm){
            nonClientOrderService.saveNonClientOrder(order, nonClientOrderForm);
        }
        return order;
    }

    // 후처리 - 쿠폰 사용
    private void postProcessingUsingCoupon(ClientOrderForm clientOrderForm) {
        // 쿠폰 사용
        Long usedCouponDiscountAmount = clientOrderForm.getCouponDiscountAmount();
        PaymentCompletedCouponResponseDto paymentCompletedCouponResponseDto = PaymentCompletedCouponResponseDto.builder()
                .couponId(clientOrderForm.getCouponId())
                .build();

        if (usedCouponDiscountAmount != null && usedCouponDiscountAmount > 0) {
            rabbitTemplate.convertAndSend(couponUseExchangeName, couponUseRoutingKey,
                    paymentCompletedCouponResponseDto);
        }
    }

    // 후처리 - 포인트 사용
    private void postProcessingUsingPoint(Long clientId,
                                          ClientOrderForm clientOrderForm) {
        Long usedPointDiscountAmount = clientOrderForm.getUsedPointDiscountAmount();
        PointUsagePaymentMessageDto pointUsagePaymentRequestDto = PointUsagePaymentMessageDto.builder()
                .pointUsagePayment(usedPointDiscountAmount)
                .clientId(clientId)
                .build();

        if (usedPointDiscountAmount != null && usedPointDiscountAmount > 0) {
            rabbitTemplate.convertAndSend(pointUseExchangeName, pointUseRoutingKey,
                    pointUsagePaymentRequestDto);
        }

    }

    private void postProcessingInventoryDecrease(Long orderId,
                                                 List<OrderDetailDtoItem> orderDetailDtoItemList) {

        Map<Long, Long> decreaseInfo = new HashMap<>();

        for (OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList) {
            decreaseInfo.put(orderDetailDtoItem.getProductId(), orderDetailDtoItem.getQuantity());
            if (Boolean.TRUE.equals(orderDetailDtoItem.getUsePackaging())) {
                decreaseInfo.put(orderDetailDtoItem.getOptionProductId(),
                        orderDetailDtoItem.getOptionQuantity());
            }
        }

        InventoryDecreaseRequestDto inventoryDecreaseRequestDto = InventoryDecreaseRequestDto.builder()
                .orderId(orderId).decreaseInfo(decreaseInfo).build();

        rabbitTemplate.convertAndSend(inventoryDecreaseExchangeName,
                inventoryDecreaseRoutingKey, inventoryDecreaseRequestDto);

    }


    // 후처리 - 회원 장바구니 데이터베이스에서 비우기
    private void postProcessingClientCartCheckout(Long clientId,
                                                  ClientOrderForm clientOrderForm) {

        CartCheckoutRequestDto cartCheckoutRequestDto = CartCheckoutRequestDto.builder()
                .clientId(clientId).build();

        clientOrderForm.getOrderItemList()
                .forEach(
                        orderDetail ->
                                cartCheckoutRequestDto.addProductId(orderDetail.getProductId())
                );

        // 구입 상품 장바구니 삭제 - 큐로 보내기
        rabbitTemplate.convertAndSend(cartCheckoutExchangeName, cartCheckoutRoutingKey,
                cartCheckoutRequestDto);

    }

    private PaymentSaveRequestDto getPaymentSaveRequestDto(ApprovePaymentRequestDto approvePaymentRequestDto){
        Long totalPayAmount = orderUtil.getOrderForm(approvePaymentRequestDto.getOrderCode()).getTotalPayAmount();
        return PaymentSaveRequestDto.builder()
                .totalPayAmount(totalPayAmount)
                .paymentMethodTypeName(approvePaymentRequestDto.getPgName())
                .paymentMethodName(paymentContext.getPaymentMethodName())
                .paymentKey(paymentContext.getPaymentKey()).build();
    }

}
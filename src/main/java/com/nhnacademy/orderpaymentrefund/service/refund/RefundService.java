package com.nhnacademy.orderpaymentrefund.service.refund;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.message.PointRewardRefundMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.message.PointUsageRefundMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.message.RefundCouponMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.PaymentCancelRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.PaymentCancelResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.PaymentRefundResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelPaymentCancel;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotCompletedException;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundPolicyRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final RefundPolicyRepository refundPolicyRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;

    @Value("${rabbit.refund.coupon.exchange.name}")
    private String refundCouponExchangeName;
    @Value("${rabbit.refund.coupon.routing.key}")
    private String refundCouponRoutingKey;

    @Value("${rabbit.refund.point.exchange.name}")
    private String refundPointExchangeName;
    @Value("${rabbit.refund.point.routing.key}")
    private String refundPointRoutingKey;

    @Value("${rabbit.usedRefund.point.exchange.name}")
    private String refundUsedPointExchangeName;
    @Value("${rabbit.usedRefund.point.routing.key}")
    private String refundUsedPointRoutingKey;

    public PaymentCancelResponseDto findOrderStatusByOrderId(long orderId) {
        PaymentCancelResponseDto dto = new PaymentCancelResponseDto();
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을수 없습니다."));

        if (order.getOrderStatus() == OrderStatus.PAYED) {
            Payment payment = paymentRepository.findByOrder_OrderId(orderId).orElseThrow(()-> new PaymentNotFoundException("결재 정보를 찾을수 없습니다."));
            dto.setPaymentId(payment.getPaymentId());
            dto.setTossPaymentKey(payment.getTossPaymentKey());
            dto.setOrderStatus(payment.getOrder()
                .getOrderStatus().toString());
            return dto;
        }
        throw new PaymentNotCompletedException("주문을 취소/반품을 처리가 불가능합니다.");
    }


    public void saveCancel(PaymentCancelRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.getOrderId())
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        if (requestDto.getOrderStatus().equals(OrderStatus.PAYED.toString())) {
            order.updateOrderStatus(OrderStatus.CANCEL);
            orderRepository.save(order);
//            List<ProductOrderDetail> prodcutOrderDetail = productOrderDetailRepository.findById(order.getOrderId()).stream().toList();
//            prodcutOrderDetail.forEach(productOrderDetail -> {
//                prodcutOrderDetail.get()
//            });
            productOrderDetailOptionRepository.findById(order.getOrderId());

            Long couponId = order.getCouponId();
            if (couponId != null){
            rabbitTemplate.convertAndSend(refundCouponExchangeName, refundCouponRoutingKey, new RefundCouponMessageDto(couponId));
            }
            Long payment = order.getOrderTotalAmount()-order.getDiscountAmountByPoint()-order.getDiscountAmountByCoupon();
            rabbitTemplate.convertAndSend(refundPointExchangeName, refundPointRoutingKey, new PointRewardRefundMessageDto(order.getClientId(), payment, order.getDiscountAmountByPoint()));
            rabbitTemplate.convertAndSend(refundUsedPointExchangeName, refundUsedPointRoutingKey, new PointUsageRefundMessageDto(order.getClientId(), order.getDiscountAmountByPoint()));
            //포인트 취소 적립포인트 사용처리 오더에서 사용포인트 돌려주고 적립으로 오더에서 구매금액 도려주고 결제에서
            // 포장지아이디 개수, 상품아이디 개수, 

        } else {
            throw new CannotCancelPaymentCancel("주문 취소에 실패하였습니다.");
        }
    }

    public PaymentRefundResponseDto findRefundData(long orderId) {
        PaymentRefundResponseDto dto = new PaymentRefundResponseDto();
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        if (order.getOrderStatus() == OrderStatus.DELIVERING
            || order.getOrderStatus() == OrderStatus.DELIVERY_COMPLETE) {
            Payment payment = paymentRepository.findByOrder_OrderId(orderId).orElseThrow(()-> new PaymentNotFoundException("결재 정보를 찾을수 없습니다."));
            dto.setTossPaymentKey(payment.getTossPaymentKey());
            dto.setPaymentId(payment.getPaymentId());
            dto.setOrderStatus(order.getOrderStatus().toString());
        } else {
            throw new CannotCancelPaymentCancel("주문 반품에 실패하였습니다.");
        }
        return dto;
    }


}
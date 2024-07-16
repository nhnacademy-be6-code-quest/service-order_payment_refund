package com.nhnacademy.orderpaymentrefund.service.refund;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.PaymentRefundResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotCompletedException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentRefundResponseDto findOrderStatusByOrderId(long orderId) {
        PaymentRefundResponseDto dto = new PaymentRefundResponseDto();
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        if (order.getOrderStatus() == OrderStatus.PAYED
            || order.getOrderStatus() == OrderStatus.DELIVERY_COMPLETE) {
            Payment payment = paymentRepository.findByOrder_OrderId(orderId);
            dto.setPaymentId(payment.getPaymentId());
            dto.setTossPaymentKey(payment.getTossPaymentKey());
            dto.setOrderStatus(payment.getOrder()
                .getOrderStatus().toString());
            return dto;
        }
        throw new PaymentNotCompletedException("주문을 취소/반품을 처리가 불가능합니다.");
    }
}
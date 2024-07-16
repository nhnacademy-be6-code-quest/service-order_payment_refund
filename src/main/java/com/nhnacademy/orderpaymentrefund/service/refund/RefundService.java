package com.nhnacademy.orderpaymentrefund.service.refund;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;

    public String findOrderStatusByOrderId(long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        OrderStatus orderStatus = order.getOrderStatus();
        return orderStatus.toString();
    }
}
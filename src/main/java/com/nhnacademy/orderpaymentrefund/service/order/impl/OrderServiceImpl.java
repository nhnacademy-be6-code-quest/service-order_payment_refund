package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.dto.order.response.service.UserDetailGetResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    @Override
    public long getTotalPrice(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
        return order.getTotalPrice();
    }

    @Override
    public List<UserDetailGetResponseDto> getUserDetails(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
        return orderDetailRepository.findByOrder(order).stream().map(
                orderDetail -> UserDetailGetResponseDto.builder()
                        .productId(orderDetail.getProductId())
                        .quantity(orderDetail.getQuantity())
                        .priceProduct(orderDetail.getProductPrice())
                        .build()
        ).toList();
    }

    @Override
    public void completePayment(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
        order.updateOrder(OrderStatus.PAYED);
        orderRepository.save(order);
    }

}

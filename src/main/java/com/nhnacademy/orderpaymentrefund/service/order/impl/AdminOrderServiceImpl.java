package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.AdminOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private OrderRepository orderRepository;
    private ProductOrderDetailRepository productOrderDetailRepository;
    private ProductOrderDetailOptionRepository productOrderDetailOptionRepository;

    @Override
    public void changeOrderStatus(long orderId, OrderStatus orderStatus) {
        orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new).updateOrderStatus(orderStatus);
    }

}
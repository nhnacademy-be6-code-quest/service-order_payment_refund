package com.nhnacademy.orderpaymentrefund.service.order.impl;


import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderStatus getOrderStatusByOrderDetailId(long orderDetailId) {
        return orderDetailRepository.findOrderStatusByOrderDetailId(orderDetailId);
    }

}

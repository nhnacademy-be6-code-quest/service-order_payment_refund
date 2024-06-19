package com.nhnacademy.order.service.impl;

import com.nhnacademy.order.dto.order.request.admin.AdminOrderPutRequestDto;
import com.nhnacademy.order.dto.order.response.admin.AdminAllOrdersGetResponseDto;
import com.nhnacademy.order.repository.OrderRepository;
import com.nhnacademy.order.service.AdminOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private OrderRepository orderRepository;

    @Override
    public List<AdminAllOrdersGetResponseDto> getAllOrders() {
        return null;
    }

    @Override
    public void updateOrder(long orderId, AdminOrderPutRequestDto adminOrderPutRequestDto) {

    }
}

package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.dto.order.request.admin.AdminOrderPutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.admin.AdminAllOrdersGetResponseDto;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.service.order.AdminOrderService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private OrderRepository orderRepository;

    @Override
    public Page<AdminAllOrdersGetResponseDto> getAllOrders() {
        return null;
    }

    @Override
    public void updateOrder(long orderId, AdminOrderPutRequestDto adminOrderPutRequestDto) {

    }

}

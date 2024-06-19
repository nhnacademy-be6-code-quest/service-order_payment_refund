package com.nhnacademy.order.service;

import com.nhnacademy.order.dto.order.request.admin.AdminOrderPutRequestDto;
import com.nhnacademy.order.dto.order.response.admin.AdminAllOrdersGetResponseDto;

import java.util.List;

public interface AdminOrderService {
    List<AdminAllOrdersGetResponseDto> getAllOrders();
    void updateOrder(long orderId, AdminOrderPutRequestDto adminOrderPutRequestDto);
}

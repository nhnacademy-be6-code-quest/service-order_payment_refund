package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.admin.AdminOrderPutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.admin.AdminAllOrdersGetResponseDto;

import java.util.List;

public interface AdminOrderService {
    List<AdminAllOrdersGetResponseDto> getAllOrders();
    void updateOrder(long orderId, AdminOrderPutRequestDto adminOrderPutRequestDto);
}

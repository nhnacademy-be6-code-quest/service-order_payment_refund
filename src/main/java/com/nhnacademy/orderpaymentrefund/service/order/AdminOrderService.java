package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.admin.AdminOrderPutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.admin.AdminAllOrdersGetResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminOrderService {
    Page<AdminAllOrdersGetResponseDto> getAllOrders();
    void updateOrder(long orderId, AdminOrderPutRequestDto adminOrderPutRequestDto);
}

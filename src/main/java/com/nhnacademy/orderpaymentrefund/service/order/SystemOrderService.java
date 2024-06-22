package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.system.SystemOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.system.SystemOrderPutRequestDto;

public interface SystemOrderService {
    void createOrder(SystemOrderPostRequestDto orderPostRequestDto);
    void updateOrder(long orderId, SystemOrderPutRequestDto orderPutRequestDto);
}

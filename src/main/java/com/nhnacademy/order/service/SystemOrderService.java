package com.nhnacademy.order.service;

import com.nhnacademy.order.dto.order.request.system.SystemOrderPostRequestDto;
import com.nhnacademy.order.dto.order.request.system.SystemOrderPutRequestDto;

public interface SystemOrderService {
    void createOrder(SystemOrderPostRequestDto orderPostRequestDto);
    void updateOrder(long orderId, SystemOrderPutRequestDto orderPutRequestDto);
}

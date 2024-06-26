package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.response.service.UserDetailGetResponseDto;

import java.util.List;

public interface OrderService {
    long getTotalPrice(long orderId);
    List<UserDetailGetResponseDto> getUserDetails(long orderId);
    void completePayment(long orderId);
}

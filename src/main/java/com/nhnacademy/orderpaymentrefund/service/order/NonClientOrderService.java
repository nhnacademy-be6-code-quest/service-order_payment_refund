package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateNonClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderDetailResponseDto;

import java.net.URI;

public interface NonClientOrderService {
    URI tryCreateOrder(CreateNonClientOrderRequestDto requestDto);
    void preprocessing();
    void postprocessing();
    void createOrder(CreateNonClientOrderRequestDto requestDto);
    FindNonClientOrderDetailResponseDto findNonClientOrderDetail(long orderId, String orderPassword);
    long findNonClientOrderId(FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto);
    String findNonClientOrderPassword(FindNonClientOrderPasswordRequestDto findNonClientOrderPasswordRequestDto);
}

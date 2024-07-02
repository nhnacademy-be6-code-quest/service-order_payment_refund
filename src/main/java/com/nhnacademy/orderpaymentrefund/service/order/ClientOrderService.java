package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.List;

public interface ClientOrderService {
    URI tryCreateOrder(HttpHeaders headers, CreateClientOrderRequestDto requestDto);
    void preprocessing();
    void postprocessing();
    void createOrder(long clientId, CreateClientOrderRequestDto requestDto);
    List<FindClientOrderResponseDto> findClientOrderList(HttpHeaders headers);
    FindClientOrderDetailResponseDto findClientOrderDetail(long orderId);
}

package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.List;

public interface ClientOrderService {
    URI tryCreateOrder(HttpHeaders headers, CreateClientOrderRequestDto requestDto);
    void preprocessing();
    void postprocessing();
    void createOrder(long clientId, CreateClientOrderRequestDto requestDto);
    Page<FindClientOrderResponseDto> findClientOrderList(HttpHeaders headers, Pageable pageable);
    FindClientOrderDetailResponseDto findClientOrderDetail(long orderId);
}

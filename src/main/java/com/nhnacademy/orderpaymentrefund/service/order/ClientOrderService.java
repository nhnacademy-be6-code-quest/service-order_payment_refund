package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;


public interface ClientOrderService {
    void tryCreateOrder(HttpHeaders headers, CreateClientOrderRequestDto requestDto);
    void preprocessing();
    void postprocessing();
    Long createOrder(long clientId, CreateClientOrderRequestDto requestDto);
    void saveOrderAndPaymentToDB();
    Page<FindClientOrderResponseDto> findClientOrderList(HttpHeaders headers, Pageable pageable);
}

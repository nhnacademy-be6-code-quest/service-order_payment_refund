package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;


public interface ClientOrderService {
    void tryCreateOrder(HttpHeaders headers, ClientOrderForm clientOrderForm);
    void preprocessing();
    void postprocessing();
    Long createOrder(long clientId, ClientOrderForm clientOrderForm);
    void saveOrderAndPaymentToDB();
    Page<FindClientOrderResponseDto> findClientOrderList(HttpHeaders headers, Pageable pageable);
}

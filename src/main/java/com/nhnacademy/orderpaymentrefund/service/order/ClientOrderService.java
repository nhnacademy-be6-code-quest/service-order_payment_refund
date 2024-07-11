package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;


public interface ClientOrderService {
    Long tryCreateOrder(HttpHeaders headers, ClientOrderFormRequestDto clientOrderForm);
    void preprocessing();
    void postprocessing();
    Long createOrder(long clientId, ClientOrderFormRequestDto clientOrderForm);
    Page<ClientOrderGetResponseDto> getOrders(HttpHeaders headers, int pageSize, int pageNo, String sortBy, String sortDir);
    ClientOrderGetResponseDto getOrder(HttpHeaders headers, long orderId);
    void paymentCompleteOrder(HttpHeaders headers, long orderId);
    void cancelOrder(HttpHeaders headers, long orderId);
    void refundOrder(HttpHeaders headers, long orderId);
    String getOrderStatus(HttpHeaders headers, long orderId);
}

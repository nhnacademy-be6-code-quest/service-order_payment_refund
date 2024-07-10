package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderListGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;


public interface ClientOrderService {
    Long tryCreateOrder(HttpHeaders headers, ClientOrderFormRequestDto clientOrderForm);
    void preprocessing();
    void postprocessing();
    Long createOrder(long clientId, ClientOrderFormRequestDto clientOrderForm);
    Page<FindClientOrderResponseDto> findClientOrderList(HttpHeaders headers, Pageable pageable);
    Page<OrderResponseDto> getOrders(HttpHeaders headers, int pageSize, int pageNo, String sortBy, String sortDir);
    OrderResponseDto getOrder(HttpHeaders headers, long orderId);
    void paymentCompleteOrder(HttpHeaders headers, long orderId);
    void cancelOrder(HttpHeaders headers, long orderId);
    void refundOrder(HttpHeaders headers, long orderId);
    String getOrderStatus(HttpHeaders headers, long orderId);
}

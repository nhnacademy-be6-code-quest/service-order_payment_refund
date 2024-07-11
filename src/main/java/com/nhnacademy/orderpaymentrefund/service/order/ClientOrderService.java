package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.List;


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
    List<ProductOrderDetailResponseDto> getProductOrderDetailList(HttpHeaders headers, Long orderId);
    ProductOrderDetailResponseDto getProductOrderDetail(HttpHeaders headers, Long orderId, Long productOrderDetailId);
    ProductOrderDetailOptionResponseDto getProductOrderDetailOptionResponseDto(HttpHeaders headers, long orderId, long detailId);

}

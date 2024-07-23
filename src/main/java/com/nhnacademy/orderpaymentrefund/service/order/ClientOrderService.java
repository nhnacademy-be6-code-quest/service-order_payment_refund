package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;


public interface ClientOrderService {
    void saveClientTemporalOrder(HttpHeaders headers, ClientOrderCreateForm clientOrderForm);
    ClientOrderCreateForm getClientTemporalOrder(HttpHeaders headers, String tossOrderId);
    Page<ClientOrderGetResponseDto> getOrders(HttpHeaders headers, int pageSize, int pageNo, String sortBy, String sortDir);
    ClientOrderGetResponseDto getOrder(HttpHeaders headers, long orderId);
    void cancelOrder(HttpHeaders headers, long orderId);
    void refundOrder(HttpHeaders headers, long orderId);
    void refundOrderRequest(HttpHeaders headers, long orderId);
    String getOrderStatus(HttpHeaders headers, long orderId);
    List<ProductOrderDetailResponseDto> getProductOrderDetailResponseDtoList(HttpHeaders headers, Long orderId);
    ProductOrderDetailResponseDto getProductOrderDetailResponseDto(HttpHeaders headers, Long orderId, Long productOrderDetailId);
    ProductOrderDetailOptionResponseDto getProductOrderDetailOptionResponseDto(HttpHeaders headers, long orderId, long detailId);

}

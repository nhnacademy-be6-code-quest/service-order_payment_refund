package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface OrderService {
    PaymentOrderShowRequestDto getPaymentOrderShowRequestDto(HttpHeaders headers, HttpServletRequest request, String orderCode);
    PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDto(HttpHeaders headers, HttpServletRequest request, String orderCode);
    void changeOrderStatus(long orderId, String orderStatus);
    Page<OrderResponseDto> getAllOrderList(int pageSize, int pageNo, String sortBy, String sortDir);
    List<ProductOrderDetailResponseDto> getProductOrderDetailList(Long orderId);
    ProductOrderDetailResponseDto getProductOrderDetail(Long orderId, Long productOrderDetailId);
    ProductOrderDetailOptionResponseDto getProductOrderDetailOptionResponseDto(long orderId, long detailId);
}

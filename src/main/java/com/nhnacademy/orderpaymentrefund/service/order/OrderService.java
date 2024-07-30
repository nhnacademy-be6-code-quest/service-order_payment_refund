package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface OrderService {
    PaymentOrderShowRequestDto getPaymentOrderShowRequestDto(String orderCode);
    PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDto(String orderCode);
    void changeOrderStatus(long orderId, String orderStatus);
    Page<OrderResponseDto> getAllOrderList(int pageSize, int pageNo, String sortBy, String sortDir);
    List<ProductOrderDetailResponseDto> getProductOrderDetailList(Long orderId);
    ProductOrderDetailResponseDto getProductOrderDetail(Long orderId, Long productOrderDetailId);
    ProductOrderDetailOptionResponseDto getProductOrderDetailOptionResponseDto(long orderId, long detailId);
}

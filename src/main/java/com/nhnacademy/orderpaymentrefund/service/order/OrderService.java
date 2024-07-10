package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    PaymentOrderShowRequestDto getPaymentOrderShowRequestDto(long orderId);
    PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDto(long orderId);
    void changeOrderStatus(long orderId, String orderStatus);
    Page<OrderResponseDto> getAllOrderList(Pageable pageable);
}

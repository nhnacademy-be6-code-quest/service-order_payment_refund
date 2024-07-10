package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;

public interface OrderService {
    PaymentOrderShowRequestDto getPaymentOrderShowRequestDto(long orderId);
    PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDto(long orderId);
    void changeOrderStatus(long orderId, String orderStatus);
}

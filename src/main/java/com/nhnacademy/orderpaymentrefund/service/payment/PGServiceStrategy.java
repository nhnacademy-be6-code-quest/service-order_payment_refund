package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.SuccessPaymentOrderInfo;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import org.json.simple.parser.ParseException;

public interface PGServiceStrategy {
    PaymentViewRequestDto getPaymentViewRequestDto(String orderCode);
    PaymentApproveResponseDto approvePayment(
            ApprovePaymentRequestDto approvePaymentRequestDto) throws ParseException;
    SuccessPaymentOrderInfo getSuccessPaymentOrderInfo(PaymentApproveResponseDto approveResponseDto, Order order, Payment payment);
    void refundPayment(long orderId, String cancelReason);
    void setPaymentKey(String paymentKey);
    void setPaymentMethodName(String paymentMethodName);
}

package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import org.json.simple.parser.ParseException;

public interface PGServiceStrategy {
    PaymentViewRequestDto getPaymentViewRequestDto(String orderCode);
    PaymentsResponseDto approvePayment(
        ApprovePaymentRequestDto approvePaymentRequestDto) throws ParseException;
    void refundPayment(long orderId, String cancelReason);
}

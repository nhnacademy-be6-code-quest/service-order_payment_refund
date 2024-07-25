package com.nhnacademy.orderpaymentrefund.service.paymentsupplier;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import org.json.simple.parser.ParseException;

public interface PaymentStrategy {
    PaymentsResponseDto approvePayment(
        ApprovePaymentRequestDto approvePaymentRequestDto) throws ParseException;
    void paymentRefund(long orderId, String cancelReason);
}

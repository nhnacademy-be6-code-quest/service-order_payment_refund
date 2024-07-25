package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

public interface PaymentStrategy {
    PaymentsResponseDto approvePayment(
        ApprovePaymentRequestDto approvePaymentRequestDto) throws ParseException;
    void refundPayment(long orderId, String cancelReason);
}

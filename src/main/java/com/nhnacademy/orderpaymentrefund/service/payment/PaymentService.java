package com.nhnacademy.orderpaymentrefund.service.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentResponseDto;

public interface PaymentService {
    public void savePayment(PaymentRequestDto paymentRequestDto);
    public PaymentResponseDto findPaymentByPaymentId(Long paymentId);
}

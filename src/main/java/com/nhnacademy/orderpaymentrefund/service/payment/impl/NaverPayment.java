package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component(value = "naver")
public class NaverPayment implements PaymentStrategy {

    @Override
    public PaymentsResponseDto approvePayment(
        ApprovePaymentRequestDto approvePaymentRequestDto) {
        return null;
    }

    @Override
    public void refundPayment(long orderId, String cancelReason) {

    }

}

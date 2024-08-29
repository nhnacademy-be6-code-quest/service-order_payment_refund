package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PGServiceStrategy;
import org.springframework.stereotype.Component;

@Component(value = "point")
public class PointPGServiceStrategy implements PGServiceStrategy {
    @Override
    public PaymentViewRequestDto getPaymentViewRequestDto(String orderCode) {
        return null;
    }

    @Override
    public PaymentsResponseDto approvePayment(
        ApprovePaymentRequestDto approvePaymentRequestDto) {
        return PaymentsResponseDto.builder()
            .methodType(approvePaymentRequestDto.getMethodType())
            .method("point")
            .orderCode(approvePaymentRequestDto.getOrderCode())
            .totalAmount(approvePaymentRequestDto.getAmount())
            .paymentKey(approvePaymentRequestDto.getPaymentKey())
            .build();
    }

    @Override
    public void refundPayment(long orderId, String cancelReason) {
        //not use
    }
}

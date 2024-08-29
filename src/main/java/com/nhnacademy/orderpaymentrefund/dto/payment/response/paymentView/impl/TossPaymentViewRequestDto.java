package com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TossPaymentViewRequestDto implements PaymentViewRequestDto {
    private final String pgName;
    private final long amount;
    private final String orderCode;
    private final String orderName;

    @Builder
    public TossPaymentViewRequestDto(long amount, String orderCode, String orderName){
        this.pgName = "toss";
        this.amount = amount;
        this.orderCode = orderCode;
        this.orderName = orderName;
    }

}

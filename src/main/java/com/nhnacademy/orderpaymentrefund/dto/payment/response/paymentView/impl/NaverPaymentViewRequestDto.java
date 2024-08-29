package com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NaverPaymentViewRequestDto implements PaymentViewRequestDto {
    private final String pgName;
    private final String productName;
    private final String orderCode;
    private final Long amount;
    private final Long taxScopeAmount;
    private final Long taxExScopeAmount;

    @Builder
    public NaverPaymentViewRequestDto(String productName, String orderCode, Long amount, Long taxScopeAmount, Long taxExScopeAmount) {
        this.pgName = "naver";
        this.productName = productName;
        this.orderCode = orderCode;
        this.amount = amount;
        this.taxScopeAmount = taxScopeAmount;
        this.taxExScopeAmount = taxExScopeAmount;
    }
}

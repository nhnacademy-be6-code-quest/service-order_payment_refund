package com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoPaymentViewRequestDto implements PaymentViewRequestDto {
    private String pgName;
    private String paymentId;
    private String redirectUrl;

    @Builder
    public KakaoPaymentViewRequestDto(String paymentId, String redirectUrl) {
        this.pgName = "kakao";
        this.paymentId = paymentId;
        this.redirectUrl = redirectUrl;
    }
}

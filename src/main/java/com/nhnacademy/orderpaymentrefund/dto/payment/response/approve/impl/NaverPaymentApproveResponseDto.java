package com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.SuccessPaymentOrderInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class NaverPaymentApproveResponseDto implements PaymentApproveResponseDto {
    @Setter
    private SuccessPaymentOrderInfo successPaymentOrderInfo;
}

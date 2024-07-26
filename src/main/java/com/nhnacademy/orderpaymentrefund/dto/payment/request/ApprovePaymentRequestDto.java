package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ApprovePaymentRequestDto {
    String orderCode; // 토스 오더 아이디
    long amount;
    String paymentKey;
    String methodType;
}

package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class TossApprovePaymentRequest {
    String orderId; // 토스 오더 아이디
    long amount;
    String paymentKey;
}

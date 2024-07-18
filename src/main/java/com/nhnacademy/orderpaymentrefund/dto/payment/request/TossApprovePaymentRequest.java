package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TossApprovePaymentRequest {
    String orderId; // 토스 오더 아이디
    long amount;
    String paymentKey;
}

package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelResponseDto {
    Long paymentId;
    String tossPaymentKey;
    String orderStatus;
}

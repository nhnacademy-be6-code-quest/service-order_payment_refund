package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRefundResponseDto {
    Long paymentId;
    String tossPaymentKey;
    String orderStatus;
}

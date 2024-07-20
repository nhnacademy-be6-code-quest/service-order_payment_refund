package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelResponseDto {
    Long paymentId;
    String tossPaymentKey;
    String orderStatus;
}

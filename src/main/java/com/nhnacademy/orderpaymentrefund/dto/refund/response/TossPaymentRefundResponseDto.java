package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

@Setter
@NoArgsConstructor
public class TossPaymentRefundResponseDto {
    String cancelReason;
        Long amount;
}

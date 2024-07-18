package com.nhnacademy.orderpaymentrefund.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponPaymentRewardRequestDto {
    Long clientId;
    Long paymentValue;
}

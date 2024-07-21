package com.nhnacademy.orderpaymentrefund.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCompletedCouponResponseDto {
    Long couponId;
}

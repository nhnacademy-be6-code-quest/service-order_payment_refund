package com.nhnacademy.orderpaymentrefund.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointRewardRefundMessageDto {
    Long clientId;
    Long payment;
    Long discountAmountByPoint;
}

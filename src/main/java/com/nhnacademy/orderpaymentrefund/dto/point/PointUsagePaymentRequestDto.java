package com.nhnacademy.orderpaymentrefund.dto.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointUsagePaymentRequestDto {
    Long pointUsageAmount;
    Long clientId;
}

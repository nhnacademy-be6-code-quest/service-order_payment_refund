package com.nhnacademy.orderpaymentrefund.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointUsagePaymentMessageDto {
    Long clientId;
    Long pointUsagePayment;
}

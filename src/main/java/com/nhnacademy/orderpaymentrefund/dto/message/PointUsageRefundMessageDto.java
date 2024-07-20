package com.nhnacademy.orderpaymentrefund.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointUsageRefundMessageDto {
    Long clientId;
    Long pointUsagePayment;
}

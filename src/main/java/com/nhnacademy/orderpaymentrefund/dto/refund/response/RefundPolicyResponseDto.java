package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class RefundPolicyResponseDto {
    Long refundPolicyId;
    String refundPolicyType;
    Integer refundShippingFee;
}

package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefundPolicyRequestDto {
    String refundAndCancelPolicyReason;
    String refundAndCancelPolicyType;
    int refundShippingFee;
}
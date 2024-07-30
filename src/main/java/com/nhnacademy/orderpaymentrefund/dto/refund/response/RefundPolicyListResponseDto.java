package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefundPolicyListResponseDto {
    private String refundPolicyType;
    private int refundShippingFee;
    private String refundPolicyIssuedDate;
    private String refundPolicyStatus;
}

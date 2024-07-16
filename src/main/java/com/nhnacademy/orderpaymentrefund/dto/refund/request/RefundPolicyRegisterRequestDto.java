package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundPolicyRegisterRequestDto {
    private String refundPolicyType;
    private Integer refundShippingFee;

}

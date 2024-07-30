package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RefundPolicyRegisterRequestDto {
    private String refundPolicyType;
    private Integer refundShippingFee;

}

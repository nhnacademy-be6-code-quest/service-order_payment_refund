package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class RefundPolicyResponseDto {
    Long refundPolicyId;
    String refundPolicyType;
    Integer refundShippingFee;
}

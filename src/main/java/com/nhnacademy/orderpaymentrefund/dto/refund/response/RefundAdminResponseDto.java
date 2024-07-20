package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class RefundAdminResponseDto {
    String refundPolicyType;
    Long refundAmount;
    String refundDateTime;
    String refundDetailReason;
}

package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RefundRequestDto {
     long orderId;
     String refundDetailReason;
     long refundPolicyId;
}

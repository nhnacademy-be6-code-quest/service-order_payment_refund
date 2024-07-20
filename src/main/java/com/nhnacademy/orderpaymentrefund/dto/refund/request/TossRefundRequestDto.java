package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class TossRefundRequestDto {
    String cancelReason;

}

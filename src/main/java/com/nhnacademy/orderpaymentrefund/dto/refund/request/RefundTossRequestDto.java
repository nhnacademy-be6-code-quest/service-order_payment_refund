package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefundTossRequestDto {
    private String cancelReason;
    private long orderId;

}

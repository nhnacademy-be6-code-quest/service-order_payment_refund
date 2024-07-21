package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PaymentCancelRequestDto {
    private String orderStatus;
    private long orderId;
    private String cancelReason;
}

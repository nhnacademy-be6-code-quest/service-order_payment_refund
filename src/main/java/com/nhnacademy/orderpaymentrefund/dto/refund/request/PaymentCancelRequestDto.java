package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PaymentCancelRequestDto {
    private String orderStatus;
    private long orderId;
}

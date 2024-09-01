package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PaymentSaveRequestDto {
    private Long totalPayAmount;
    private String paymentMethodTypeName; // pgName
    private String paymentMethodName;
    private String paymentKey;
}

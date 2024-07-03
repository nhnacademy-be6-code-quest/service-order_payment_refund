package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentRequestDto {
    long amount;

    @NotNull
    String orderId;

    @NotNull
    String paymentKey;
}

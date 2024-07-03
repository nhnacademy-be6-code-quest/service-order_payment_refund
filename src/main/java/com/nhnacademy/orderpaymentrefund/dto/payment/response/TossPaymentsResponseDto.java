package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class TossPaymentsResponseDto {
    @NotBlank
    String orderName;

    long totalAmount;

    @NotBlank
    String method;

    @NotBlank
    String paymentKey;

    @Nullable
    @Setter
    String cardNumber;          // method 가 카드일 때

    @Setter
    @Nullable
    String accountNumber;       // method 가 가상계좌일 때

    @Setter
    @Nullable
    String bank;                // method 가 계좌이체일 때

    @Setter
    @Nullable
    String customerMobilePhone; // method 가 휴대폰일 때
}
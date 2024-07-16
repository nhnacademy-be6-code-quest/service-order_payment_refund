package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentGradeResponseDto {
    Long paymentGradeValue;
}
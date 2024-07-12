package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentGradeResponseDto {
    Long paymentGradeValue;
}
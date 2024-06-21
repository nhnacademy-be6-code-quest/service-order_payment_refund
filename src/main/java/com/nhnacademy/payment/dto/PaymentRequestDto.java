package com.nhnacademy.payment.dto;

public record PaymentRequestDto(
    Long orderId,
    Long paymentMethodId,
    Long couponId
) {
}
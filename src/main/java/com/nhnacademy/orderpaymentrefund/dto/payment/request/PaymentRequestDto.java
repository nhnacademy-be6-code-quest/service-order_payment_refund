package com.nhnacademy.orderpaymentrefund.dto.payment.request;

public record PaymentRequestDto(
    Long orderId,
    Long paymentMethodId,
    Long couponId,
    Long payAmount
) {
}
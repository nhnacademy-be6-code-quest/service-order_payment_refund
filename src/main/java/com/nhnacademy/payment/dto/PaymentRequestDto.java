package com.nhnacademy.payment.dto;

// DB 에 박을 거 == 사용자가 보내는 거
public record PaymentRequestDto(
    Long orderId,
    Long clientDeliveryAddressId,
    Long paymentMethodId,
    Long couponId
) {
}
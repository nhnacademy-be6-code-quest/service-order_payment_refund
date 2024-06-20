package com.nhnacademy.payment.dto;

import java.time.LocalDateTime;

// DB 에 박을 거 == 사용자가 보내는 거
public record PaymentRequestDto(
    Long orderId,
    LocalDateTime payTime,
    Long clientDeliveryAddressId,
    Long paymentMethodId,
    Long couponId
) {
}
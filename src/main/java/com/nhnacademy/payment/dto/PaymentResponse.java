package com.nhnacademy.payment.dto;

import com.nhnacademy.payment.domain.Payment;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @author Virtus_Chae
 * @version 1.0
 */
// 결제를 조회할 때 나와야 하는 정보들
@Builder
public record PaymentResponse (
        Long paymentId,
        Long orderId,
        LocalDateTime payTime,
        Long clientDeliveryAddressId,
        String paymentMethodName,
        Long couponId
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getPayTime(),
                payment.getClientDeliveryAddressId(),
                payment.getPaymentMethod().getPaymentMethodName(),
                payment.getCouponId()
        );
    }
}
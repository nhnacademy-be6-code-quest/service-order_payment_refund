package com.nhnacademy.payment.dto;

import com.nhnacademy.payment.domain.Payment;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 결제가 완성되었을 때 받을 수 있는 정보들입니다.
 * 결제 내역을 조회하는 등의 상황에 사용됩니다.
 *
 * @author Virtus_Chae
 * @version 1.0
 */

@Builder
public record PaymentResponse(
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
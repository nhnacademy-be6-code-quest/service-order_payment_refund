package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApprovePaymentRequestDtoTest {

    @Test
    void testDefaultConstructor() {
        ApprovePaymentRequestDto request = new ApprovePaymentRequestDto();
        assertThat(request).isNotNull();
        assertThat(request.getOrderCode()).isNull(); // 기본값은 null
        assertThat(request.getAmount()).isZero(); // 기본값은 0
        assertThat(request.getPaymentKey()).isNull(); // 기본값은 null
    }

    @Test
    void testSetterAndGetter() {
        String orderId = "order123";
        long amount = 5000L;
        String paymentKey = "key123";

        ApprovePaymentRequestDto request = new ApprovePaymentRequestDto();
        request.orderCode = orderId; // Lombok의 @Setter를 사용하지 않지만 직접 필드에 접근
        request.amount = amount; // Lombok의 @Setter를 사용하지 않지만 직접 필드에 접근
        request.paymentKey = paymentKey; // Lombok의 @Setter를 사용하지 않지만 직접 필드에 접근

        assertThat(request.getOrderCode()).isEqualTo(orderId);
        assertThat(request.getAmount()).isEqualTo(amount);
        assertThat(request.getPaymentKey()).isEqualTo(paymentKey);
    }
}

package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PaymentGradeResponseDtoTest {

    @Test
    void testBuilder() {
        Long paymentGradeValue = 5L;

        PaymentGradeResponseDto dto = PaymentGradeResponseDto.builder()
            .paymentGradeValue(paymentGradeValue)
            .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getPaymentGradeValue()).isEqualTo(paymentGradeValue);
    }

    @Test
    void testNoArgsConstructor() {
        PaymentGradeResponseDto dto = new PaymentGradeResponseDto();

        assertThat(dto).isNotNull();
        assertThat(dto.getPaymentGradeValue()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        Long paymentGradeValue = 10L;

        PaymentGradeResponseDto dto = new PaymentGradeResponseDto(paymentGradeValue);

        assertThat(dto).isNotNull();
        assertThat(dto.getPaymentGradeValue()).isEqualTo(paymentGradeValue);
    }
}

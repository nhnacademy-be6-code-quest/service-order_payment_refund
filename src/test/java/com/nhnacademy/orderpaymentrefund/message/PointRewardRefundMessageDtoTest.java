package com.nhnacademy.orderpaymentrefund.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.message.PointRewardRefundMessageDto;
import org.junit.jupiter.api.Test;

class PointRewardRefundMessageDtoTest {
    @Test
    public void testNoArgsConstructor() {
        // Given
        // No given values since we are testing the no-arg constructor

        // When
        PointRewardRefundMessageDto dto = new PointRewardRefundMessageDto();

        // Then
        assertThat(dto.getClientId()).isNull();
        assertThat(dto.getPayment()).isNull();
        assertThat(dto.getDiscountAmountByPoint()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Given
        Long clientId = 12345L;
        Long payment = 500L;
        Long discountAmountByPoint = 100L;

        // When
        PointRewardRefundMessageDto dto = new PointRewardRefundMessageDto(clientId, payment, discountAmountByPoint);

        // Then
        assertThat(dto.getClientId()).isEqualTo(clientId);
        assertThat(dto.getPayment()).isEqualTo(payment);
        assertThat(dto.getDiscountAmountByPoint()).isEqualTo(discountAmountByPoint);
    }

}

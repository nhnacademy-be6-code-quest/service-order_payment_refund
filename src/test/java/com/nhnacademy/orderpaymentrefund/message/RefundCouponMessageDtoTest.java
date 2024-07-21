package com.nhnacademy.orderpaymentrefund.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.message.RefundCouponMessageDto;
import org.junit.jupiter.api.Test;

class RefundCouponMessageDtoTest {
    @Test
    public void testNoArgsConstructor() {
        // Given
        // No given values since we are testing the no-arg constructor

        // When
        RefundCouponMessageDto dto = new RefundCouponMessageDto();

        // Then
        assertThat(dto.getCouponId()).isEqualTo(0); // Default long value is 0
    }

    @Test
    public void testAllArgsConstructor() {
        // Given
        long couponId = 12345L;

        // When
        RefundCouponMessageDto dto = new RefundCouponMessageDto(couponId);

        // Then
        assertThat(dto.getCouponId()).isEqualTo(couponId);
    }

}
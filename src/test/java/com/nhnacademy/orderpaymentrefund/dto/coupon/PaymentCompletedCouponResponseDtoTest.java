package com.nhnacademy.orderpaymentrefund.dto.coupon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PaymentCompletedCouponResponseDtoTest {

    @Test
    void testPaymentCompletedCouponResponseDtoCreationWithBuilder() {
        // Given
        Long couponId = 123L;

        // When
        PaymentCompletedCouponResponseDto dto = PaymentCompletedCouponResponseDto.builder()
            .couponId(couponId)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(couponId, dto.getCouponId());
    }

    @Test
    void testPaymentCompletedCouponResponseDtoWithNullCouponId() {
        // Given
        Long couponId = null;

        // When
        PaymentCompletedCouponResponseDto dto = PaymentCompletedCouponResponseDto.builder()
            .couponId(couponId)
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getCouponId());
    }

    @Test
    void testPaymentCompletedCouponResponseDtoWithNoArgsConstructor() {
        // Given
        PaymentCompletedCouponResponseDto dto =  PaymentCompletedCouponResponseDto.builder().couponId(456L).build();

        // When

        // Then
        assertNotNull(dto);
        assertEquals(456L, dto.getCouponId());
    }

    @Test
    void testPaymentCompletedCouponResponseDtoWithAllArgsConstructor() {
        // Given
        Long couponId = 789L;

        // When
        PaymentCompletedCouponResponseDto dto = new PaymentCompletedCouponResponseDto(couponId);

        // Then
        assertNotNull(dto);
        assertEquals(couponId, dto.getCouponId());
    }
}

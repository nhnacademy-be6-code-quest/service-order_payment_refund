package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundCouponResponseDtoTest {

    @Test
    void testRefundCouponResponseDtoWithDefaultConstructor() {
        // When
        RefundCouponResponseDto dto = new RefundCouponResponseDto();

        // Then
        assertNotNull(dto);
        assertEquals(0L, dto.getCouponId()); // Default value for long
    }

    @Test
    void testSettersAndGetters() {
        // Given
        long couponId = 12345L;

        RefundCouponResponseDto dtoWithValue = new RefundCouponResponseDto(12345L);


        // Then
        assertEquals(couponId, dtoWithValue.getCouponId());
    }
}

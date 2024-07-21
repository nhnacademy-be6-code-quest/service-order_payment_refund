package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundSuccessResponseDtoTest {

    @Test
    void testRefundSuccessResponseDtoWithAllArgsConstructor() {
        // Given
        Long refundAmount = 100L;

        // When
        RefundSuccessResponseDto dto = new RefundSuccessResponseDto(refundAmount);

        // Then
        assertNotNull(dto);
        assertEquals(refundAmount, dto.getRefundAmount());
    }

    @Test
    void testRefundSuccessResponseDtoWithDefaultConstructor() {
        // When
        RefundSuccessResponseDto dto = new RefundSuccessResponseDto();

        // Then
        assertNotNull(dto);
        assertNull(dto.getRefundAmount()); // Default value for Long is null
    }

    @Test
    void testSettersAndGetters() {
        // Given
        Long refundAmount = 200L;

        // When
        RefundSuccessResponseDto dto = new RefundSuccessResponseDto(refundAmount);

        // Then
        assertEquals(refundAmount, dto.getRefundAmount());
    }
}

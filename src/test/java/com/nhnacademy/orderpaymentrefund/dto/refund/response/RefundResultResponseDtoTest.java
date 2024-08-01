package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundResultResponseDtoTest {

    @Test
    void testRefundResultResponseDtoWithAllArgsConstructor() {
        // Given
        String cancelReason = "Item damaged";
        String methodTYpe = "toss";

        // When
        RefundResultResponseDto dto = new RefundResultResponseDto(cancelReason, methodTYpe);

        // Then
        assertNotNull(dto);
        assertEquals(cancelReason, dto.getCancelReason());
    }

    @Test
    void testRefundResultResponseDtoWithDefaultConstructor() {
        // When
        RefundResultResponseDto dto = new RefundResultResponseDto();

        // Then
        assertNotNull(dto);
        assertNull(dto.getCancelReason()); // Default value for String
    }
}

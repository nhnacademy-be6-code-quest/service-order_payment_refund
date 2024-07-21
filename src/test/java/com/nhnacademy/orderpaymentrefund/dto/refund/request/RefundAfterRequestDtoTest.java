package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundAfterRequestDtoTest {

    @Test
    void testRefundAfterRequestDtoWithNoArgsConstructor() {
        // Given
        RefundAfterRequestDto dto = new RefundAfterRequestDto();

        // When
        dto.setOrderId(123L);

        // Then
        assertNotNull(dto);
        assertEquals(123L, dto.getOrderId());
    }

    @Test
    void testRefundAfterRequestDtoFieldValues() {
        // Given
        Long orderId = 123L;

        // When
        RefundAfterRequestDto dto = new RefundAfterRequestDto();
        dto.setOrderId(orderId);

        // Then
        assertEquals(orderId, dto.getOrderId());
    }

    @Test
    void testRefundAfterRequestDtoDefaultValues() {
        // Given
        RefundAfterRequestDto dto = new RefundAfterRequestDto();

        // When & Then
        assertNull(dto.getOrderId());
    }
}

package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundTossRequestDtoTest {

    @Test
    void testRefundTossRequestDtoWithBuilder() {
        // Given
        String cancelReason = "Incorrect charge";
        long orderId = 12345L;

        // When
        RefundTossRequestDto dto = RefundTossRequestDto.builder()
            .cancelReason(cancelReason)
            .orderId(orderId)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(cancelReason, dto.getCancelReason());
        assertEquals(orderId, dto.getOrderId());
    }

    @Test
    void testRefundTossRequestDtoWithPartialBuilder() {
        // Given
        String cancelReason = "Product defective";

        // When
        RefundTossRequestDto dto = RefundTossRequestDto.builder()
            .cancelReason(cancelReason)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(cancelReason, dto.getCancelReason());
        assertEquals(0L, dto.getOrderId()); // Default value for long
    }

    @Test
    void testRefundTossRequestDtoWithDefaultValues() {
        // When
        RefundTossRequestDto dto = RefundTossRequestDto.builder()
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getCancelReason()); // Default value for String
        assertEquals(0L, dto.getOrderId()); // Default value for long
    }
}

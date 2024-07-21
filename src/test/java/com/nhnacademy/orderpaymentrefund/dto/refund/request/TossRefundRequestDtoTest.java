package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TossRefundRequestDtoTest {

    @Test
    void testTossRefundRequestDtoWithBuilder() {
        // Given
        String cancelReason = "Order was canceled";

        // When
        TossRefundRequestDto dto = TossRefundRequestDto.builder()
            .cancelReason(cancelReason)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(cancelReason, dto.getCancelReason());
    }

    @Test
    void testTossRefundRequestDtoWithSetter() {
        // Given
        TossRefundRequestDto dto = new TossRefundRequestDto("Incorrect order");
        String cancelReason = "Incorrect order";

        // When
        dto.setCancelReason(cancelReason);

        // Then
        assertNotNull(dto);
        assertEquals(cancelReason, dto.getCancelReason());
    }

    @Test
    void testTossRefundRequestDtoDefaultValues() {
        // When
        TossRefundRequestDto dto = TossRefundRequestDto.builder()
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getCancelReason()); // Default value for String
    }
}

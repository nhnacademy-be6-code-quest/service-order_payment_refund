package com.nhnacademy.orderpaymentrefund.dto.point;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PointUsagePaymentRequestDtoTest {

    @Test
    void testPointUsagePaymentRequestDtoCreationWithBuilder() {
        // Given
        Long pointUsageAmount = 1000L;
        Long clientId = 123L;

        // When
        PointUsagePaymentRequestDto dto = PointUsagePaymentRequestDto.builder()
            .pointUsageAmount(pointUsageAmount)
            .clientId(clientId)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(pointUsageAmount, dto.getPointUsageAmount());
        assertEquals(clientId, dto.getClientId());
    }

    @Test
    void testPointUsagePaymentRequestDtoWithNullValues() {
        // Given
        Long pointUsageAmount = null;
        Long clientId = null;

        // When
        PointUsagePaymentRequestDto dto = PointUsagePaymentRequestDto.builder()
            .pointUsageAmount(pointUsageAmount)
            .clientId(clientId)
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getPointUsageAmount());
        assertNull(dto.getClientId());
    }

    @Test
    void testPointUsagePaymentRequestDtoWithNoArgsConstructor() {
        // Given
        PointUsagePaymentRequestDto dto = new PointUsagePaymentRequestDto();

        // When
        dto = PointUsagePaymentRequestDto.builder()
            .pointUsageAmount(500L)
            .clientId(456L)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(500L, dto.getPointUsageAmount());
        assertEquals(456L, dto.getClientId());
    }

    @Test
    void testPointUsagePaymentRequestDtoWithAllArgsConstructor() {
        // Given
        Long pointUsageAmount = 2000L;
        Long clientId = 789L;

        // When
        PointUsagePaymentRequestDto dto = new PointUsagePaymentRequestDto(pointUsageAmount, clientId);

        // Then
        assertNotNull(dto);
        assertEquals(pointUsageAmount, dto.getPointUsageAmount());
        assertEquals(clientId, dto.getClientId());
    }
}

package com.nhnacademy.orderpaymentrefund.dto.client;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ClientUpdateGradeRequestDtoTest {

    @Test
    void testClientUpdateGradeRequestDtoCreation() {
        // Given
        Long clientId = 123L;
        Long payment = 5000L;

        // When
        ClientUpdateGradeRequestDto dto = ClientUpdateGradeRequestDto.builder()
            .clientId(clientId)
            .payment(payment)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(clientId, dto.getClientId());
        assertEquals(payment, dto.getPayment());
    }

    @Test
    void testClientUpdateGradeRequestDtoWithNullValues() {
        // Given
        Long clientId = null;
        Long payment = null;

        // When
        ClientUpdateGradeRequestDto dto = ClientUpdateGradeRequestDto.builder()
            .clientId(clientId)
            .payment(payment)
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getClientId());
        assertNull(dto.getPayment());
    }

    @Test
    void testClientUpdateGradeRequestDtoWithOnlyClientId() {
        // Given
        Long clientId = 123L;
        Long payment = null;

        // When
        ClientUpdateGradeRequestDto dto = ClientUpdateGradeRequestDto.builder()
            .clientId(clientId)
            .payment(payment)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(clientId, dto.getClientId());
        assertNull(dto.getPayment());
    }

    @Test
    void testClientUpdateGradeRequestDtoWithOnlyPayment() {
        // Given
        Long clientId = null;
        Long payment = 5000L;

        // When
        ClientUpdateGradeRequestDto dto = ClientUpdateGradeRequestDto.builder()
            .clientId(clientId)
            .payment(payment)
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getClientId());
        assertEquals(payment, dto.getPayment());
    }
}

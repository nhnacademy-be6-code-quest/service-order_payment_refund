package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TossPGServiceStrategyRefundResponseDtoTest {

    @Test
    void testTossPaymentRefundResponseDtoWithDefaultConstructor() {
        // When
        TossPaymentRefundResponseDto dto = new TossPaymentRefundResponseDto();

        // Then
        assertNotNull(dto);
        assertNull(dto.getCancelReason()); // Default value for String is null
        assertNull(dto.getAmount()); // Default value for Long is null
    }

    @Test
    void testSettersAndGetters() {
        // Given
        String cancelReason = "Reason for cancellation";
        Long amount = 150L;

        // When
        TossPaymentRefundResponseDto dto = new TossPaymentRefundResponseDto();
        dto.setCancelReason(cancelReason);
        dto.setAmount(amount);

        // Then
        assertEquals(cancelReason, dto.getCancelReason());
        assertEquals(amount, dto.getAmount());
    }
}

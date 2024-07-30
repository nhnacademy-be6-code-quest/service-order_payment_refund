package com.nhnacademy.orderpaymentrefund.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class RefundPolicyNotFoundExceptionTest {
    @Test
    void testRefundPolicyNotFoundException() {
        // Given
        String errorMessage = "Refund policy not found";

        // When
        RefundPolicyNotFoundException exception = new RefundPolicyNotFoundException(errorMessage);

        // Then
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(errorMessage, exception.getMessage(), "Exception message should match");
    }
}

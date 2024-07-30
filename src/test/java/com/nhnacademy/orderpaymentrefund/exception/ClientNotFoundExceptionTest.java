package com.nhnacademy.orderpaymentrefund.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ClientNotFoundExceptionTest {
    @Test
    void testClientNotFoundException() {
        // Given
        String errorMessage = "Client not found";

        // When
        ClientNotFoundException exception = new ClientNotFoundException(errorMessage);

        // Then
        assertNotNull(exception, "Exception should be instantiated");
        assertEquals(errorMessage, exception.getMessage(), "Exception message should match");
    }
}

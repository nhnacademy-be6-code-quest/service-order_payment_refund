package com.nhnacademy.orderpaymentrefund.exception.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class NotFoundExceptionTypeTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String expectedMessage = "Resource not found";

        // Act
        NotFoundExceptionType exception = new NotFoundExceptionType(expectedMessage);

        // Assert
        assertNotNull(exception, "The exception should not be null.");
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected message.");
    }
}

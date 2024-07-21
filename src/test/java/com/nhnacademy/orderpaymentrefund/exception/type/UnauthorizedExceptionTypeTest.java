package com.nhnacademy.orderpaymentrefund.exception.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UnauthorizedExceptionTypeTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String expectedMessage = "Unauthorized access";

        // Act
        UnauthorizedExceptionType exception = new UnauthorizedExceptionType(expectedMessage);

        // Assert
        assertNotNull(exception, "The exception should not be null.");
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected message.");
    }
}

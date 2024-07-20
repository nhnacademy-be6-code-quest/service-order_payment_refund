package com.nhnacademy.orderpaymentrefund.exception.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ForbiddenExceptionTypeTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String expectedMessage = "Access is forbidden";

        // Act
        ForbiddenExceptionType exception = new ForbiddenExceptionType(expectedMessage);

        // Assert
        assertNotNull(exception, "The exception should not be null.");
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected message.");
    }
}

package com.nhnacademy.orderpaymentrefund.exception.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BadRequestExceptionTypeTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String expectedMessage = "Bad request occurred";

        // Act
        BadRequestExceptionType exception = new BadRequestExceptionType(expectedMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage(), "The exception message should match the expected message.");
    }
}

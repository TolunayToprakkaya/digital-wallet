package com.example.digitalwallet.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidTransactionTypeExceptionTest {

    @Test
    void testInvalidTransactionTypeExceptionMessage() {
        // Arrange
        String expectedType = "DEPOSIT";

        // Act
        InvalidTransactionTypeException exception = assertThrows(InvalidTransactionTypeException.class, () -> {
            throw new InvalidTransactionTypeException(expectedType);
        });

        // Assert
        assertEquals("Geçersiz işlem tipi. Yalnızca '" + expectedType + "' tipi desteklenmektedir.", exception.getMessage());
    }
}


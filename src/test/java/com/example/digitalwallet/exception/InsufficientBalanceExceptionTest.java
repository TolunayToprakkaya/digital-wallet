package com.example.digitalwallet.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InsufficientBalanceExceptionTest {

    @Test
    void testInsufficientBalanceExceptionMessage() {
        // Arrange
        String message = "Insufficient balance for the withdrawal";

        // Act
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            throw new InsufficientBalanceException(message);
        });

        // Assert
        assertEquals(message, exception.getMessage());
    }
}

package com.example.digitalwallet.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidTransactionStatusExceptionTest {

    @Test
    void testInvalidTransactionStatusExceptionMessage() {
        // Arrange
        String currentStatus = "APPROVED";

        // Act
        InvalidTransactionStatusException exception = assertThrows(InvalidTransactionStatusException.class, () -> {
            throw new InvalidTransactionStatusException(currentStatus);
        });

        // Assert
        assertEquals("Sadece PENDING durumundaki işlemler onaylanabilir. Mevcut durum: " + currentStatus, exception.getMessage());
    }
}


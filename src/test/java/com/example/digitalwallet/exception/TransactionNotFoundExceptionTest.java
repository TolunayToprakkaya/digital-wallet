package com.example.digitalwallet.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionNotFoundExceptionTest {

    @Test
    void testTransactionNotFoundExceptionMessage() {
        // Arrange
        Integer transactionId = 123;

        // Act
        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> {
            throw new TransactionNotFoundException(transactionId);
        });

        // Assert
        assertEquals("İşlem bulunamadı. ID: " + transactionId, exception.getMessage());
    }
}


package com.example.digitalwallet.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleInsufficientBalance() {
        String expectedMessage = "Insufficient balance for the withdrawal";
        InsufficientBalanceException exception = new InsufficientBalanceException(expectedMessage);

        ResponseEntity<String> response = globalExceptionHandler.handleInsufficientBalance(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void handleTransactionNotFound() {
        String expectedMessage = "İşlem bulunamadı. ID: 123";
        TransactionNotFoundException exception = new TransactionNotFoundException(123);

        ResponseEntity<String> response = globalExceptionHandler.handleTransactionNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void handleInvalidTransactionType() {
        String expectedMessage = "Geçersiz işlem tipi. Yalnızca 'DEPOSIT' tipi desteklenmektedir.";
        InvalidTransactionTypeException exception = new InvalidTransactionTypeException("DEPOSIT");

        ResponseEntity<String> response = globalExceptionHandler.handleInvalidTransactionType(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void handleInvalidTransactionStatus() {
        String expectedMessage = "Sadece PENDING durumundaki işlemler onaylanabilir. Mevcut durum: COMPLETED";
        InvalidTransactionStatusException exception = new InvalidTransactionStatusException("COMPLETED");

        ResponseEntity<String> response = globalExceptionHandler.handleInvalidTransactionStatus(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    void handleIllegalArgument() {
        String expectedMessage = "Geçersiz istek: Invalid parameter";
        IllegalArgumentException exception = new IllegalArgumentException("Invalid parameter");

        ResponseEntity<String> response = globalExceptionHandler.handleIllegalArgument(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Geçersiz istek: Invalid parameter", response.getBody());
    }
}


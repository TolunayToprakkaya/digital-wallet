package com.example.digitalwallet.exception;

public class InvalidTransactionTypeException extends RuntimeException {
    public InvalidTransactionTypeException(String expectedType) {
        super("Geçersiz işlem tipi. Yalnızca '" + expectedType + "' tipi desteklenmektedir.");
    }
}

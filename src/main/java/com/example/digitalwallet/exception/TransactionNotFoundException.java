package com.example.digitalwallet.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Integer transactionId) {
        super("İşlem bulunamadı. ID: " + transactionId);
    }
}

package com.example.digitalwallet.exception;

public class InvalidTransactionStatusException extends RuntimeException {
    public InvalidTransactionStatusException(String currentStatus) {
        super("Sadece PENDING durumundaki işlemler onaylanabilir. Mevcut durum: " + currentStatus);
    }
}

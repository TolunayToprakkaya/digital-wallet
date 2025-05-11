package com.example.digitalwallet.enumaration;

import lombok.Getter;

@Getter
public enum TransactionStatus {

    PENDING(1),
    APPROVED(2),
    DENIED(3);

    final int statusId;

    TransactionStatus(int statusId) {
        this.statusId = statusId;
    }

}

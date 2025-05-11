package com.example.digitalwallet.enumaration;

import lombok.Getter;

@Getter
public enum TransactionTypeStatus {

    DEPOSIT(1),
    WITHDRAW(2);

    final int statusId;

    TransactionTypeStatus(int statusId) {
        this.statusId = statusId;
    }

}

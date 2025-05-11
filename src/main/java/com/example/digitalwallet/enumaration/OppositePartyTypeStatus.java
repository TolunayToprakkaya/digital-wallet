package com.example.digitalwallet.enumaration;

import lombok.Getter;

@Getter
public enum OppositePartyTypeStatus {

    IBAN(1),
    PAYMENT(2);

    final int statusId;

    OppositePartyTypeStatus(int statusId) {
        this.statusId = statusId;
    }

}

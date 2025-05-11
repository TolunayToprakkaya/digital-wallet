package com.example.digitalwallet.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWalletRequest {
    private String name;
    private String currency;
    private boolean activeForShopping;
    private boolean activeForWithdraw;
}

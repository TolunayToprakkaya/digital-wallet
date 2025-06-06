package com.example.digitalwallet.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawRequest {
    private BigDecimal amount;
    private String walletId;
    private String destination;
}

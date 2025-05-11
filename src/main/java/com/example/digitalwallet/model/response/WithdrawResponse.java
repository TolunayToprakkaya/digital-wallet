package com.example.digitalwallet.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class WithdrawResponse {
    private String name;
    private String currency;
    private BigDecimal balance;
    private BigDecimal usableBalance;
}

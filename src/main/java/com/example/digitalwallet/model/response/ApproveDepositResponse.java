package com.example.digitalwallet.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApproveDepositResponse {
    private String walletId;
    private BigDecimal balance;
    private BigDecimal usableBalance;
    private String status;
}

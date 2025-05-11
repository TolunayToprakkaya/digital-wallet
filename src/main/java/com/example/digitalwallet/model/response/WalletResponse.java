package com.example.digitalwallet.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {
    private String name;
    private String currency;
    private boolean isActiveForShopping;
    private boolean isActiveForWithdraw;
    private BigDecimal balance;
    private BigDecimal usableBalance;
}

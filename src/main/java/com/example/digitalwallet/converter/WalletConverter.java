package com.example.digitalwallet.converter;

import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.model.request.CreateWalletRequest;
import com.example.digitalwallet.model.response.WalletResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class WalletConverter {

    public Wallet apply(String customerId, CreateWalletRequest request) {
        return Wallet.builder()
                .walletId(UUID.randomUUID().toString())
                .customerId(customerId)
                .name(request.getName())
                .currency(request.getCurrency())
                .isActiveForShopping(request.isActiveForShopping())
                .isActiveForWithdraw(request.isActiveForWithdraw())
                .balance(BigDecimal.ZERO)
                .usableBalance(BigDecimal.ZERO)
                .build();
    }

    public WalletResponse apply(Wallet wallet) {
        return WalletResponse.builder()
                .name(wallet.getName())
                .currency(wallet.getCurrency())
                .isActiveForShopping(wallet.isActiveForShopping())
                .isActiveForWithdraw(wallet.isActiveForWithdraw())
                .balance(wallet.getBalance())
                .usableBalance(wallet.getUsableBalance())
                .build();
    }
}

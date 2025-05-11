package com.example.digitalwallet.service;

import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.model.request.DepositRequest;
import com.example.digitalwallet.model.response.DepositResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositService {

    private final WalletService walletService;
    private final TransactionService transactionService;

    public DepositService(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    public DepositResponse deposit(DepositRequest request) {
        Wallet wallet = walletService.getWalletById(request.getWalletId());
        BigDecimal amount = request.getAmount();

        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            wallet.setBalance(wallet.getBalance().add(amount));
        } else {
            wallet.setBalance(wallet.getBalance().add(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
        }

        walletService.save(wallet);
        transactionService.saveDeposit(request, wallet);
        return DepositResponse.builder()
                .name(wallet.getName())
                .currency(wallet.getCurrency())
                .balance(wallet.getBalance())
                .usableBalance(wallet.getUsableBalance())
                .build();
    }
}

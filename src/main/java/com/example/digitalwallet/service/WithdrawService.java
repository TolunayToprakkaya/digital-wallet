package com.example.digitalwallet.service;

import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.exception.InsufficientBalanceException;
import com.example.digitalwallet.model.request.DepositRequest;
import com.example.digitalwallet.model.request.WithdrawRequest;
import com.example.digitalwallet.model.response.DepositResponse;
import com.example.digitalwallet.model.response.WithdrawResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithdrawService {

    private final WalletService walletService;
    private final TransactionService transactionService;

    public WithdrawService(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    public WithdrawResponse withdraw(WithdrawRequest request) {
        Wallet wallet = walletService.getWalletById(request.getWalletId());
        BigDecimal amount = request.getAmount();
        BigDecimal currentUsableBalance = wallet.getUsableBalance();

        if (currentUsableBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient Balance: Amount to be withdrawn "
                    + amount + " but current balance " + currentUsableBalance);
        }

        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        } else {
            wallet.setBalance(wallet.getBalance().subtract(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        }

        walletService.save(wallet);
        transactionService.saveWithdraw(request, wallet);
        return WithdrawResponse.builder()
                .name(wallet.getName())
                .currency(wallet.getCurrency())
                .balance(wallet.getBalance())
                .usableBalance(wallet.getUsableBalance())
                .build();
    }
}

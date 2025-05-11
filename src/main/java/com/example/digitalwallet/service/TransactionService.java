package com.example.digitalwallet.service;

import com.example.digitalwallet.converter.TransactionConverter;
import com.example.digitalwallet.entity.Transaction;
import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.enumaration.TransactionStatus;
import com.example.digitalwallet.enumaration.TransactionTypeStatus;
import com.example.digitalwallet.exception.InvalidTransactionStatusException;
import com.example.digitalwallet.exception.InvalidTransactionTypeException;
import com.example.digitalwallet.exception.TransactionNotFoundException;
import com.example.digitalwallet.model.request.ApproveRequest;
import com.example.digitalwallet.model.request.DepositRequest;
import com.example.digitalwallet.model.request.WithdrawRequest;
import com.example.digitalwallet.model.response.ApproveDepositResponse;
import com.example.digitalwallet.model.response.ApproveWithdrawResponse;
import com.example.digitalwallet.model.response.TransactionResponse;
import com.example.digitalwallet.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionConverter transactionConverter;
    private final WalletService walletService;

    public TransactionService(TransactionRepository transactionRepository, TransactionConverter transactionConverter, WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.transactionConverter = transactionConverter;
        this.walletService = walletService;
    }

    public void saveDeposit(DepositRequest depositRequest, Wallet wallet) {
        Transaction transaction = transactionConverter.apply(depositRequest, wallet);
        transactionRepository.save(transaction);
    }

    public void saveWithdraw(WithdrawRequest withdrawRequest, Wallet wallet) {
        Transaction transaction = transactionConverter.apply(withdrawRequest, wallet);
        transactionRepository.save(transaction);
    }

    public List<TransactionResponse> getTransactionListByWalletId(String walletId) {
        return transactionRepository.findAllByWalletId(walletId).stream()
                .map(transaction -> {
                    TransactionResponse response = transactionConverter.apply(transaction);
                    response.setId(transaction.getId());
                    response.setWalletId(transaction.getWalletId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getCustomerTransactionListByWalletId(String walletId) {
        return transactionRepository.findAllByWalletId(walletId).stream()
                .map(transactionConverter::apply)
                .collect(Collectors.toList());
    }

    public ApproveDepositResponse approveDeposit(ApproveRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new TransactionNotFoundException(request.getTransactionId()));

        if (!TransactionTypeStatus.DEPOSIT.name().equals(transaction.getType())) {
            throw new InvalidTransactionTypeException(TransactionTypeStatus.DEPOSIT.name());
        }

        if (!TransactionStatus.PENDING.name().equals(transaction.getStatus())) {
            throw new InvalidTransactionStatusException(transaction.getStatus());
        }

        Wallet wallet = walletService.getWalletById(transaction.getWalletId());

        if (TransactionStatus.APPROVED.name().equalsIgnoreCase(request.getStatus())) {
            wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
            transaction.setStatus(TransactionStatus.APPROVED.name());
        } else if (TransactionStatus.DENIED.name().equalsIgnoreCase(request.getStatus())) {
            wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
            transaction.setStatus(TransactionStatus.DENIED.name());
        } else {
            throw new IllegalArgumentException("Geçersiz onay durumu: " + request.getStatus());
        }

        transactionRepository.save(transaction);
        walletService.save(wallet);

        return ApproveDepositResponse.builder()
                .walletId(wallet.getWalletId())
                .balance(wallet.getBalance())
                .usableBalance(wallet.getUsableBalance())
                .status(transaction.getStatus())
                .build();
    }

    public ApproveWithdrawResponse approveWithdraw(ApproveRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new TransactionNotFoundException(request.getTransactionId()));

        if (!TransactionTypeStatus.WITHDRAW.name().equals(transaction.getType())) {
            throw new InvalidTransactionTypeException(TransactionTypeStatus.WITHDRAW.name());
        }

        if (!TransactionStatus.PENDING.name().equals(transaction.getStatus())) {
            throw new InvalidTransactionStatusException(transaction.getStatus());
        }

        Wallet wallet = walletService.getWalletById(transaction.getWalletId());

        if (TransactionStatus.APPROVED.name().equalsIgnoreCase(request.getStatus())) {
            wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(transaction.getAmount()));
            transaction.setStatus(TransactionStatus.APPROVED.name());
        } else if (TransactionStatus.DENIED.name().equalsIgnoreCase(request.getStatus())) {
            wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
            transaction.setStatus(TransactionStatus.DENIED.name());
        } else {
            throw new IllegalArgumentException("Geçersiz onay durumu: " + request.getStatus());
        }

        transactionRepository.save(transaction);
        walletService.save(wallet);

        return ApproveWithdrawResponse.builder()
                .walletId(wallet.getWalletId())
                .balance(wallet.getBalance())
                .usableBalance(wallet.getUsableBalance())
                .status(transaction.getStatus())
                .build();
    }

}

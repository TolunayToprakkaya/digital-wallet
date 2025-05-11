package com.example.digitalwallet.converter;

import com.example.digitalwallet.entity.Transaction;
import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.enumaration.OppositePartyTypeStatus;
import com.example.digitalwallet.enumaration.TransactionStatus;
import com.example.digitalwallet.enumaration.TransactionTypeStatus;
import com.example.digitalwallet.model.request.DepositRequest;
import com.example.digitalwallet.model.request.WithdrawRequest;
import com.example.digitalwallet.model.response.TransactionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionConverter {

    public TransactionResponse apply(Transaction transaction) {
        return TransactionResponse.builder()
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .oppositePartyType(transaction.getOppositePartyType())
                .oppositeParty(transaction.getOppositeParty())
                .status(transaction.getStatus())
                .build();
    }

    public Transaction apply(DepositRequest depositRequest, Wallet wallet) {
        String source = depositRequest.getSource();
        String oppositePartyType = source != null && source.toUpperCase().matches("^TR\\d{2}[0-9A-Z]{1,30}$")
                ? OppositePartyTypeStatus.IBAN.name()
                : OppositePartyTypeStatus.PAYMENT.name();

        String status = depositRequest.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0
                ? TransactionStatus.PENDING.name()
                : TransactionStatus.APPROVED.name();

        return Transaction.builder()
                .walletId(wallet.getWalletId())
                .amount(depositRequest.getAmount())
                .type(TransactionTypeStatus.DEPOSIT.name())
                .oppositePartyType(oppositePartyType)
                .oppositeParty(source)
                .status(status)
                .build();
    }


    public Transaction apply(WithdrawRequest withdrawRequest, Wallet wallet) {
        String destination = withdrawRequest.getDestination();
        String oppositePartyType = destination != null && destination.toUpperCase().matches("^TR\\d{2}[0-9A-Z]{1,30}$")
                ? OppositePartyTypeStatus.IBAN.name()
                : OppositePartyTypeStatus.PAYMENT.name();

        String status = withdrawRequest.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0
                ? TransactionStatus.PENDING.name()
                : TransactionStatus.APPROVED.name();

        return Transaction.builder()
                .walletId(wallet.getWalletId())
                .amount(withdrawRequest.getAmount())
                .type(TransactionTypeStatus.WITHDRAW.name())
                .oppositePartyType(oppositePartyType)
                .oppositeParty(destination)
                .status(status)
                .build();
    }
}

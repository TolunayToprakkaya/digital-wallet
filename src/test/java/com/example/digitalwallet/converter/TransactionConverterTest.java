package com.example.digitalwallet.converter;

import com.example.digitalwallet.entity.Transaction;
import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.enumaration.OppositePartyTypeStatus;
import com.example.digitalwallet.enumaration.TransactionStatus;
import com.example.digitalwallet.enumaration.TransactionTypeStatus;
import com.example.digitalwallet.model.request.DepositRequest;
import com.example.digitalwallet.model.request.WithdrawRequest;
import com.example.digitalwallet.model.response.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionConverterTest {

    private TransactionConverter transactionConverter;
    private Wallet wallet;
    private DepositRequest depositRequest;
    private WithdrawRequest withdrawRequest;

    @BeforeEach
    void setUp() {
        transactionConverter = new TransactionConverter();

        wallet = new Wallet();
        wallet.setWalletId("wallet123");

        depositRequest = new DepositRequest();
        depositRequest.setAmount(BigDecimal.valueOf(500));
        depositRequest.setSource("TR123456789012345678901234567890");

        withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAmount(BigDecimal.valueOf(1500));
        withdrawRequest.setDestination("TR987654321098765432109876543210");
    }

    @Test
    void testApplyTransactionResponse() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(500));
        transaction.setType(TransactionTypeStatus.DEPOSIT.name());
        transaction.setOppositePartyType(OppositePartyTypeStatus.PAYMENT.name());
        transaction.setOppositeParty("TR123456789012345678901234567890");
        transaction.setStatus(TransactionStatus.APPROVED.name());

        // Act
        TransactionResponse response = transactionConverter.apply(transaction);

        // Assert
        assertNotNull(response);
        assertEquals(transaction.getAmount(), response.getAmount());
        assertEquals(transaction.getType(), response.getType());
        assertEquals(transaction.getOppositePartyType(), response.getOppositePartyType());
        assertEquals(transaction.getOppositeParty(), response.getOppositeParty());
        assertEquals(transaction.getStatus(), response.getStatus());
    }

    @Test
    void testApplyDepositTransaction() {
        // Act
        Transaction transaction = transactionConverter.apply(depositRequest, wallet);

        // Assert
        assertNotNull(transaction);
        assertEquals(wallet.getWalletId(), transaction.getWalletId());
        assertEquals(depositRequest.getAmount(), transaction.getAmount());
        assertEquals(TransactionTypeStatus.DEPOSIT.name(), transaction.getType());
        assertEquals(OppositePartyTypeStatus.IBAN.name(), transaction.getOppositePartyType());
        assertEquals(depositRequest.getSource(), transaction.getOppositeParty());
        assertEquals(TransactionStatus.APPROVED.name(), transaction.getStatus());
    }

    @Test
    void testApplyWithdrawTransaction() {
        // Act
        Transaction transaction = transactionConverter.apply(withdrawRequest, wallet);

        // Assert
        assertNotNull(transaction);
        assertEquals(wallet.getWalletId(), transaction.getWalletId());
        assertEquals(withdrawRequest.getAmount(), transaction.getAmount());
        assertEquals(TransactionTypeStatus.WITHDRAW.name(), transaction.getType());
        assertEquals(OppositePartyTypeStatus.IBAN.name(), transaction.getOppositePartyType());
        assertEquals(withdrawRequest.getDestination(), transaction.getOppositeParty());
        assertEquals(TransactionStatus.PENDING.name(), transaction.getStatus());
    }

    @Test
    void testApplyDepositTransactionWithDifferentSource() {
        // Arrange
        depositRequest.setSource("PaymentMethod");

        // Act
        Transaction transaction = transactionConverter.apply(depositRequest, wallet);

        // Assert
        assertNotNull(transaction);
        assertEquals(wallet.getWalletId(), transaction.getWalletId());
        assertEquals(depositRequest.getAmount(), transaction.getAmount());
        assertEquals(TransactionTypeStatus.DEPOSIT.name(), transaction.getType());
        assertEquals(OppositePartyTypeStatus.PAYMENT.name(), transaction.getOppositePartyType());
        assertEquals(depositRequest.getSource(), transaction.getOppositeParty());
        assertEquals(TransactionStatus.APPROVED.name(), transaction.getStatus());
    }

    @Test
    void testApplyWithdrawTransactionWithDifferentDestination() {
        // Arrange
        withdrawRequest.setDestination("PaymentMethod");

        // Act
        Transaction transaction = transactionConverter.apply(withdrawRequest, wallet);

        // Assert
        assertNotNull(transaction);
        assertEquals(wallet.getWalletId(), transaction.getWalletId());
        assertEquals(withdrawRequest.getAmount(), transaction.getAmount());
        assertEquals(TransactionTypeStatus.WITHDRAW.name(), transaction.getType());
        assertEquals(OppositePartyTypeStatus.PAYMENT.name(), transaction.getOppositePartyType());
        assertEquals(withdrawRequest.getDestination(), transaction.getOppositeParty());
        assertEquals(TransactionStatus.PENDING.name(), transaction.getStatus());
    }
}


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
import com.example.digitalwallet.service.TransactionService;
import com.example.digitalwallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionConverter transactionConverter;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDeposit() {
        DepositRequest request = new DepositRequest();
        Wallet wallet = new Wallet();
        Transaction transaction = new Transaction();

        when(transactionConverter.apply(request, wallet)).thenReturn(transaction);

        transactionService.saveDeposit(request, wallet);

        verify(transactionRepository).save(transaction);
    }

    @Test
    void testSaveWithdraw() {
        WithdrawRequest request = new WithdrawRequest();
        Wallet wallet = new Wallet();
        Transaction transaction = new Transaction();

        when(transactionConverter.apply(request, wallet)).thenReturn(transaction);

        transactionService.saveWithdraw(request, wallet);

        verify(transactionRepository).save(transaction);
    }

    @Test
    void testGetTransactionListByWalletId() {
        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setWalletId("wallet123");

        TransactionResponse response = new TransactionResponse();

        when(transactionRepository.findAllByWalletId("wallet123")).thenReturn(List.of(transaction));
        when(transactionConverter.apply(transaction)).thenReturn(response);

        List<TransactionResponse> result = transactionService.getTransactionListByWalletId("wallet123");

        assertEquals(1, result.size());
        verify(transactionConverter).apply(transaction);
    }

    @Test
    void testGetCustomerTransactionListByWalletId() {
        Transaction transaction = new Transaction();
        TransactionResponse response = new TransactionResponse();

        when(transactionRepository.findAllByWalletId("wallet123")).thenReturn(List.of(transaction));
        when(transactionConverter.apply(transaction)).thenReturn(response);

        List<TransactionResponse> result = transactionService.getCustomerTransactionListByWalletId("wallet123");

        assertEquals(1, result.size());
    }

    @Test
    void testApproveDepositApproved() {
        ApproveRequest request = new ApproveRequest(1, "APPROVED");
        Transaction transaction = new Transaction(1, "wallet1", BigDecimal.valueOf(10), TransactionTypeStatus.DEPOSIT.name(), "PENDING", "", "PENDING");
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.TEN);
        wallet.setUsableBalance(BigDecimal.ZERO);

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        when(walletService.getWalletById("wallet1")).thenReturn(wallet);

        var result = transactionService.approveDeposit(request);

        assertEquals("APPROVED", result.getStatus());
        assertEquals(BigDecimal.TEN, result.getUsableBalance());
    }

    @Test
    void testApproveWithdrawDenied() {
        ApproveRequest request = new ApproveRequest(1, "DENIED");
        Transaction transaction = new Transaction(1, "wallet1", BigDecimal.valueOf(10), TransactionTypeStatus.WITHDRAW.name(), "PENDING", "", "PENDING");
        Wallet wallet = new Wallet();
        wallet.setUsableBalance(BigDecimal.ZERO);

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        when(walletService.getWalletById("wallet1")).thenReturn(wallet);

        var result = transactionService.approveWithdraw(request);

        assertEquals("DENIED", result.getStatus());
    }

    @Test
    void testApproveDepositInvalidType() {
        ApproveRequest request = new ApproveRequest(1, "APPROVED");
        Transaction transaction = new Transaction(1, "wallet1", BigDecimal.valueOf(10), TransactionTypeStatus.WITHDRAW.name(), "PENDING", "", "");

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        assertThrows(InvalidTransactionTypeException.class, () -> transactionService.approveDeposit(request));
    }

    @Test
    void testApproveWithdrawInvalidStatus() {
        ApproveRequest request = new ApproveRequest(1, "APPROVED");
        Transaction transaction = new Transaction(1, "wallet1", BigDecimal.valueOf(10), TransactionTypeStatus.WITHDRAW.name(), "DENIED", "", "");

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        assertThrows(InvalidTransactionStatusException.class, () -> transactionService.approveWithdraw(request));
    }

    @Test
    void testApproveWithdrawNotFound() {
        ApproveRequest request = new ApproveRequest(1, "APPROVED");

        when(transactionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.approveWithdraw(request));
    }

    @Test
    void testApproveDepositIllegalArgument() {
        ApproveRequest request = new ApproveRequest(1, "UNKNOWN");
        Transaction transaction = new Transaction(1, "wallet1", BigDecimal.valueOf(10), TransactionTypeStatus.DEPOSIT.name(), "PENDING", "", "");

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        assertThrows(InvalidTransactionStatusException.class, () -> transactionService.approveDeposit(request));
    }

    @Test
    void testApproveDeposit_Denied() {
        ApproveRequest request = new ApproveRequest();
        request.setTransactionId(1);
        request.setStatus("DENIED");

        Wallet wallet = new Wallet();
        wallet.setWalletId("wallet123");
        wallet.setBalance(BigDecimal.valueOf(500));
        wallet.setUsableBalance(BigDecimal.valueOf(300));

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setWalletId("wallet123");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType("DEPOSIT");
        transaction.setStatus("PENDING");

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        when(walletService.getWalletById("wallet123")).thenReturn(wallet);

        ApproveDepositResponse response = transactionService.approveDeposit(request);

        assertEquals("DENIED", response.getStatus());
        assertEquals(BigDecimal.valueOf(400), response.getBalance()); // 500 - 100
        verify(transactionRepository).save(transaction);
        verify(walletService).save(wallet);
    }

    @Test
    void testApproveDeposit_InvalidStatus() {
        ApproveRequest request = new ApproveRequest();
        request.setTransactionId(1);
        request.setStatus("UNKNOWN_STATUS");

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setWalletId("wallet123");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType("DEPOSIT");
        transaction.setStatus("PENDING");

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        assertThrows(IllegalArgumentException.class, () -> transactionService.approveDeposit(request));
    }

    @Test
    void testApproveWithdraw_InvalidTransactionType() {
        ApproveRequest request = new ApproveRequest();
        request.setTransactionId(1);
        request.setStatus("APPROVED");

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setWalletId("wallet123");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType("DEPOSIT"); // yanlış type
        transaction.setStatus("PENDING");

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        assertThrows(InvalidTransactionTypeException.class, () -> transactionService.approveWithdraw(request));
    }

    @Test
    void testApproveWithdraw_InvalidStatus() {
        ApproveRequest request = new ApproveRequest();
        request.setTransactionId(1);
        request.setStatus("UNKNOWN");

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setWalletId("wallet123");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType("WITHDRAW");
        transaction.setStatus("PENDING");

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        when(walletService.getWalletById("wallet123")).thenReturn(new Wallet());

        assertThrows(IllegalArgumentException.class, () -> transactionService.approveWithdraw(request));
    }

    @Test
    void testApproveWithdraw_ApprovedStatus() {
        ApproveRequest request = new ApproveRequest();
        request.setTransactionId(1);
        request.setStatus("APPROVED");

        // Wallet ve Transaction nesnelerini oluşturuyoruz
        Wallet wallet = new Wallet();
        wallet.setWalletId("wallet123");
        wallet.setBalance(BigDecimal.valueOf(500));
        wallet.setUsableBalance(BigDecimal.valueOf(300));

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setWalletId("wallet123");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setType("WITHDRAW");
        transaction.setStatus("PENDING");

        // Mocking işlemleri
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        when(walletService.getWalletById("wallet123")).thenReturn(wallet);

        // Metodun çağrılması
        ApproveWithdrawResponse response = transactionService.approveWithdraw(request);

        // Assert işlemleri
        assertEquals("APPROVED", response.getStatus());
        assertEquals(BigDecimal.valueOf(400), response.getBalance()); // 500 - 100
        assertEquals(BigDecimal.valueOf(200), response.getUsableBalance()); // 300 - 100
        verify(transactionRepository).save(transaction);
        verify(walletService).save(wallet);
    }


}


package com.example.digitalwallet.service;

import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.exception.InsufficientBalanceException;
import com.example.digitalwallet.model.request.WithdrawRequest;
import com.example.digitalwallet.model.response.WithdrawResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WithdrawServiceTest {

    @Mock
    private WalletService walletService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private WithdrawService withdrawService;

    private Wallet wallet;
    private WithdrawRequest withdrawRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        wallet = new Wallet();
        wallet.setWalletId("wallet123");
        wallet.setBalance(BigDecimal.valueOf(2000));
        wallet.setUsableBalance(BigDecimal.valueOf(1500));

        withdrawRequest = new WithdrawRequest();
        withdrawRequest.setWalletId("wallet123");
    }

    @Test
    void testWithdrawSuccessWithBalanceUpdate() {
        // Arrange
        BigDecimal withdrawAmount = BigDecimal.valueOf(500);
        withdrawRequest.setAmount(withdrawAmount);

        when(walletService.getWalletById("wallet123")).thenReturn(wallet);

        // Act
        WithdrawResponse response = withdrawService.withdraw(withdrawRequest);

        // Assert
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(1500).subtract(withdrawAmount), response.getUsableBalance());
        assertEquals(BigDecimal.valueOf(2000).subtract(withdrawAmount), response.getBalance());
        verify(walletService).save(wallet);
        verify(transactionService).saveWithdraw(withdrawRequest, wallet);
    }

    @Test
    void testWithdrawSuccessWithUsableBalanceUpdate() {
        // Arrange
        BigDecimal withdrawAmount = BigDecimal.valueOf(1000);
        withdrawRequest.setAmount(withdrawAmount);

        when(walletService.getWalletById("wallet123")).thenReturn(wallet);

        // Act
        WithdrawResponse response = withdrawService.withdraw(withdrawRequest);

        // Assert
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(1500).subtract(withdrawAmount), response.getUsableBalance());
        assertEquals(BigDecimal.valueOf(1000), response.getBalance());
        verify(walletService).save(wallet);
        verify(transactionService).saveWithdraw(withdrawRequest, wallet);
    }

    @Test
    void testWithdrawInsufficientBalance() {
        // Arrange
        BigDecimal withdrawAmount = BigDecimal.valueOf(2000);
        withdrawRequest.setAmount(withdrawAmount);

        when(walletService.getWalletById("wallet123")).thenReturn(wallet);

        // Act & Assert
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            withdrawService.withdraw(withdrawRequest);
        });

        assertEquals("Insufficient Balance: Amount to be withdrawn 2000 but current balance 1500", exception.getMessage());
    }

    @Test
    void testWithdrawAmountGreaterThanLimit() {
        // Arrange
        BigDecimal withdrawAmount = BigDecimal.valueOf(1200);
        withdrawRequest.setAmount(withdrawAmount);

        when(walletService.getWalletById("wallet123")).thenReturn(wallet);

        // Act
        WithdrawResponse response = withdrawService.withdraw(withdrawRequest);

        // Assert
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(1500).subtract(withdrawAmount), response.getUsableBalance());
        assertEquals(BigDecimal.valueOf(2000), response.getBalance());
        verify(walletService).save(wallet);
        verify(transactionService).saveWithdraw(withdrawRequest, wallet);
    }
}


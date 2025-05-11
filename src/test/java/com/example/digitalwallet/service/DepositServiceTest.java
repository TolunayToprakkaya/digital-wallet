package com.example.digitalwallet.service;

import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.model.request.DepositRequest;
import com.example.digitalwallet.model.response.DepositResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @Mock
    private WalletService walletService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private DepositService depositService;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.setWalletId("wallet123");
        wallet.setName("My Wallet");
        wallet.setCurrency("TRY");
        wallet.setBalance(BigDecimal.valueOf(5000));
        wallet.setUsableBalance(BigDecimal.valueOf(3000));
    }

    @Test
    void testDepositAmountGreaterThan1000() {
        DepositRequest request = new DepositRequest();
        request.setWalletId("wallet123");
        request.setAmount(BigDecimal.valueOf(1500));
        request.setSource("TR000011223344");

        when(walletService.getWalletById("wallet123")).thenReturn(wallet);

        DepositResponse response = depositService.deposit(request);

        assertEquals("My Wallet", response.getName());
        assertEquals("TRY", response.getCurrency());
        assertEquals(BigDecimal.valueOf(6500), response.getBalance()); // 5000 + 1500
        assertEquals(BigDecimal.valueOf(3000), response.getUsableBalance()); // No change

        verify(walletService).save(wallet);
        verify(transactionService).saveDeposit(request, wallet);
    }

    @Test
    void testDepositAmountLessThanOrEqual1000() {
        DepositRequest request = new DepositRequest();
        request.setWalletId("wallet123");
        request.setAmount(BigDecimal.valueOf(500));
        request.setSource("TR000011223344");

        when(walletService.getWalletById("wallet123")).thenReturn(wallet);

        DepositResponse response = depositService.deposit(request);

        assertEquals("My Wallet", response.getName());
        assertEquals("TRY", response.getCurrency());
        assertEquals(BigDecimal.valueOf(5500), response.getBalance()); // 5000 + 500
        assertEquals(BigDecimal.valueOf(3500), response.getUsableBalance()); // 3000 + 500

        verify(walletService).save(wallet);
        verify(transactionService).saveDeposit(request, wallet);
    }
}


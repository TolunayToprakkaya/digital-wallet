package com.example.digitalwallet.controller;

import com.example.digitalwallet.model.request.CreateWalletRequest;
import com.example.digitalwallet.model.response.WalletResponse;
import com.example.digitalwallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateWallet() {
        // Arrange
        CreateWalletRequest request = new CreateWalletRequest();
        request.setName("Main Wallet");
        request.setCurrency("USD");
        request.setActiveForShopping(true);
        request.setActiveForWithdraw(true);

        WalletResponse expectedResponse = WalletResponse.builder()
                .name("Main Wallet")
                .currency("USD")
                .balance(BigDecimal.ZERO)
                .usableBalance(BigDecimal.ZERO)
                .build();

        UserDetails userDetails = new User("customer1", "password", List.of());

        when(walletService.createWallet(userDetails.getUsername(), request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<WalletResponse> response = walletController.createWallet(userDetails, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(walletService).createWallet("customer1", request);
    }

    @Test
    public void testGetWalletListByCustomerId() {
        // Arrange
        String customerId = "customer123";
        String currency = "EUR";
        BigDecimal minBalance = new BigDecimal("100");
        BigDecimal maxBalance = new BigDecimal("1000");

        WalletResponse wallet1 = WalletResponse.builder()
                .name("Shopping Wallet")
                .currency("EUR")
                .balance(new BigDecimal("500"))
                .usableBalance(new BigDecimal("400"))
                .build();

        List<WalletResponse> expectedList = Arrays.asList(wallet1);

        when(walletService.getWalletListByCustomerId(customerId, currency, minBalance, maxBalance))
                .thenReturn(expectedList);

        // Act
        ResponseEntity<List<WalletResponse>> response = walletController.getWalletListByCustomerId(
                customerId, currency, minBalance, maxBalance);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedList, response.getBody());
        verify(walletService).getWalletListByCustomerId(customerId, currency, minBalance, maxBalance);
    }
}


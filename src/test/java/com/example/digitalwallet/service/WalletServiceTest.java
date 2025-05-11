package com.example.digitalwallet.service;

import com.example.digitalwallet.converter.WalletConverter;
import com.example.digitalwallet.entity.Customer;
import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.model.request.CreateWalletRequest;
import com.example.digitalwallet.model.response.WalletResponse;
import com.example.digitalwallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletConverter walletConverter;

    @Mock
    private CustomerDetailsService customerDetailsService;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWalletById() {
        // Arrange
        String walletId = "wallet123";
        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(BigDecimal.valueOf(1000));

        when(walletRepository.findByWalletId(walletId)).thenReturn(wallet);

        // Act
        Wallet result = walletService.getWalletById(walletId);

        // Assert
        assertNotNull(result);
        assertEquals(walletId, result.getWalletId());
        assertEquals(BigDecimal.valueOf(1000), result.getBalance());
    }

    @Test
    void testSaveWallet() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setWalletId("wallet123");
        wallet.setBalance(BigDecimal.valueOf(500));

        when(walletRepository.save(wallet)).thenReturn(wallet);

        // Act
        Wallet savedWallet = walletService.save(wallet);

        // Assert
        assertNotNull(savedWallet);
        assertEquals("wallet123", savedWallet.getWalletId());
        assertEquals(BigDecimal.valueOf(500), savedWallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void testCreateWallet() {
        // Arrange
        String customerEmail = "test@example.com";
        CreateWalletRequest request = new CreateWalletRequest();
        request.setCurrency("USD");

        Customer customer = new Customer();
        customer.setCustomerId("customer123");

        Wallet wallet = new Wallet();
        wallet.setWalletId("wallet123");
        wallet.setCurrency("USD");
        wallet.setBalance(BigDecimal.valueOf(1000));

        WalletResponse expectedResponse = new WalletResponse();
        expectedResponse.setName("wallet123");
        expectedResponse.setCurrency("USD");
        expectedResponse.setBalance(BigDecimal.valueOf(1000));

        when(customerDetailsService.findByEmail(customerEmail)).thenReturn(customer);
        when(walletConverter.apply(customer.getCustomerId(), request)).thenReturn(wallet);
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(walletConverter.apply(wallet)).thenReturn(expectedResponse);

        // Act
        WalletResponse response = walletService.createWallet(customerEmail, request);

        // Assert
        assertNotNull(response);
        assertEquals("wallet123", response.getName());
        assertEquals("USD", response.getCurrency());
        assertEquals(BigDecimal.valueOf(1000), response.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void testGetWalletListByCustomerId() {
        // Arrange
        String customerId = "customer123";
        String currency = "USD";
        BigDecimal minBalance = BigDecimal.valueOf(500);
        BigDecimal maxBalance = BigDecimal.valueOf(2000);

        Wallet wallet1 = new Wallet();
        wallet1.setWalletId("wallet123");
        wallet1.setCustomerId(customerId);
        wallet1.setCurrency("USD");
        wallet1.setBalance(BigDecimal.valueOf(1000));

        Wallet wallet2 = new Wallet();
        wallet2.setWalletId("wallet124");
        wallet2.setCustomerId(customerId);
        wallet2.setCurrency("USD");
        wallet2.setBalance(BigDecimal.valueOf(1500));

        List<Wallet> walletList = List.of(wallet1, wallet2);

        when(walletRepository.findAllByCustomerId(customerId)).thenReturn(walletList);

        // Act
        List<WalletResponse> response = walletService.getWalletListByCustomerId(customerId, currency, minBalance, maxBalance);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void testGetWalletListByCustomerIdWithFilters() {
        // Arrange
        String customerId = "customer123";
        String currency = "USD";
        BigDecimal minBalance = BigDecimal.valueOf(500);
        BigDecimal maxBalance = BigDecimal.valueOf(1000);

        Wallet wallet1 = new Wallet();
        wallet1.setWalletId("wallet123");
        wallet1.setCustomerId(customerId);
        wallet1.setCurrency("USD");
        wallet1.setBalance(BigDecimal.valueOf(1000));

        Wallet wallet2 = new Wallet();
        wallet2.setWalletId("wallet124");
        wallet2.setCustomerId(customerId);
        wallet2.setCurrency("USD");
        wallet2.setBalance(BigDecimal.valueOf(2000));

        List<Wallet> walletList = List.of(wallet1, wallet2);

        when(walletRepository.findAllByCustomerId(customerId)).thenReturn(walletList);

        // Act
        List<WalletResponse> response = walletService.getWalletListByCustomerId(customerId, currency, minBalance, maxBalance);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
    }
}


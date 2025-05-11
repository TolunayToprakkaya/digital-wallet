package com.example.digitalwallet.controller;

import com.example.digitalwallet.model.request.DepositRequest;
import com.example.digitalwallet.model.response.DepositResponse;
import com.example.digitalwallet.service.DepositService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DepositControllerTest {

    @Mock
    private DepositService depositService;

    @InjectMocks
    private DepositController depositController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeposit() {
        // Arrange
        DepositRequest request = new DepositRequest();
        request.setAmount(BigDecimal.valueOf(500));
        request.setWalletId("wallet123");
        request.setSource("TR123456789");

        DepositResponse expectedResponse = DepositResponse.builder()
                .name("Main Wallet")
                .currency("TRY")
                .balance(BigDecimal.valueOf(1500))
                .usableBalance(BigDecimal.valueOf(1500))
                .build();

        when(depositService.deposit(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<DepositResponse> response = depositController.deposit(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(depositService).deposit(request);
    }
}
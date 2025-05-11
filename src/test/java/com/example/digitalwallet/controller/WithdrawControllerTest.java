package com.example.digitalwallet.controller;

import com.example.digitalwallet.model.request.WithdrawRequest;
import com.example.digitalwallet.model.response.WithdrawResponse;
import com.example.digitalwallet.service.WithdrawService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WithdrawControllerTest {

    private WithdrawService withdrawService;
    private WithdrawController withdrawController;

    @BeforeEach
    void setUp() {
        withdrawService = mock(WithdrawService.class);
        withdrawController = new WithdrawController(withdrawService);
    }

    @Test
    void testWithdraw() {
        // Arrange
        WithdrawRequest request = new WithdrawRequest();
        request.setWalletId("wallet123");
        request.setAmount(BigDecimal.valueOf(200));
        request.setDestination("TR123456789");

        WithdrawResponse expectedResponse = WithdrawResponse.builder()
                .name("Test User")
                .currency("TRY")
                .balance(BigDecimal.valueOf(800))
                .usableBalance(BigDecimal.valueOf(800))
                .build();

        when(withdrawService.withdraw(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<WithdrawResponse> response = withdrawController.withdraw(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(withdrawService, times(1)).withdraw(request);
    }
}


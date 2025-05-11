package com.example.digitalwallet.controller;

import com.example.digitalwallet.model.request.ApproveRequest;
import com.example.digitalwallet.model.response.ApproveDepositResponse;
import com.example.digitalwallet.model.response.ApproveWithdrawResponse;
import com.example.digitalwallet.model.response.TransactionResponse;
import com.example.digitalwallet.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTransactionListByWalletId() {
        String walletId = "wallet123";
        List<TransactionResponse> expectedList = Arrays.asList(
                new TransactionResponse(1, "12345", BigDecimal.valueOf(500), "DEPOSIT", "APPROVED", "", ""),
                new TransactionResponse(2, "123456", BigDecimal.valueOf(300), "WITHDRAW", "PENDING", "", "")
        );

        when(transactionService.getTransactionListByWalletId(walletId)).thenReturn(expectedList);

        ResponseEntity<List<TransactionResponse>> response = transactionController.getTransactionListByWalletId(walletId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedList, response.getBody());
        verify(transactionService).getTransactionListByWalletId(walletId);
    }

    @Test
    public void testGetCustomerTransactionListByWalletId() {
        String walletId = "wallet456";
        List<TransactionResponse> expectedList = Arrays.asList(
                new TransactionResponse(3, "123456", BigDecimal.valueOf(1000), "DEPOSIT", "APPROVED", "", "")
        );

        when(transactionService.getCustomerTransactionListByWalletId(walletId)).thenReturn(expectedList);

        ResponseEntity<List<TransactionResponse>> response = transactionController.getCustomerTransactionListByWalletId(walletId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedList, response.getBody());
        verify(transactionService).getCustomerTransactionListByWalletId(walletId);
    }

    @Test
    public void testApproveDeposit() {
        ApproveRequest request = new ApproveRequest();
        request.setTransactionId(1);
        request.setStatus("APPROVED");

        ApproveDepositResponse expectedResponse = new ApproveDepositResponse("wallet123", BigDecimal.valueOf(2000), BigDecimal.valueOf(1500), "APPROVED");

        when(transactionService.approveDeposit(request)).thenReturn(expectedResponse);

        ResponseEntity<ApproveDepositResponse> response = transactionController.approveDeposit(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(transactionService).approveDeposit(request);
    }

    @Test
    public void testApproveWithdraw() {
        ApproveRequest request = new ApproveRequest();
        request.setTransactionId(1);
        request.setStatus("APPROVED");

        ApproveWithdrawResponse expectedResponse = new ApproveWithdrawResponse("wallet789", BigDecimal.valueOf(1800), BigDecimal.valueOf(1800), "APPROVED");

        when(transactionService.approveWithdraw(request)).thenReturn(expectedResponse);

        ResponseEntity<ApproveWithdrawResponse> response = transactionController.approveWithdraw(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(transactionService).approveWithdraw(request);
    }
}


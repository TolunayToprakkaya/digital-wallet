package com.example.digitalwallet.controller;

import com.example.digitalwallet.model.request.ApproveRequest;
import com.example.digitalwallet.model.request.WithdrawRequest;
import com.example.digitalwallet.model.response.ApproveDepositResponse;
import com.example.digitalwallet.model.response.ApproveWithdrawResponse;
import com.example.digitalwallet.model.response.TransactionResponse;
import com.example.digitalwallet.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // admin
    @GetMapping("/{walletId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionListByWalletId(@PathVariable String walletId) {
        return ResponseEntity.ok(transactionService.getTransactionListByWalletId(walletId));
    }

    // user
    @GetMapping("/customer/{walletId}")
    public ResponseEntity<List<TransactionResponse>> getCustomerTransactionListByWalletId(@PathVariable String walletId) {
        return ResponseEntity.ok(transactionService.getCustomerTransactionListByWalletId(walletId));
    }

    //admin
    @PostMapping("/approve/deposit")
    public ResponseEntity<ApproveDepositResponse> approveDeposit(@RequestBody ApproveRequest request) {
        return ResponseEntity.ok(transactionService.approveDeposit(request));
    }

    //admin
    @PostMapping("/approve/withdraw")
    public ResponseEntity<ApproveWithdrawResponse> approveWithdraw(@RequestBody ApproveRequest request) {
        return ResponseEntity.ok(transactionService.approveWithdraw(request));
    }
}

package com.example.digitalwallet.controller;

import com.example.digitalwallet.model.request.WithdrawRequest;
import com.example.digitalwallet.model.response.WithdrawResponse;
import com.example.digitalwallet.service.WithdrawService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/withdraw")
public class WithdrawController {

    private final WithdrawService withdrawService;

    public WithdrawController(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    //user
    @PostMapping
    public ResponseEntity<WithdrawResponse> withdraw(@RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(withdrawService.withdraw(request));
    }
}

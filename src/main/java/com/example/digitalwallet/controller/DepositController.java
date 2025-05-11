package com.example.digitalwallet.controller;

import com.example.digitalwallet.model.request.DepositRequest;
import com.example.digitalwallet.model.response.DepositResponse;
import com.example.digitalwallet.service.DepositService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deposit")
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    //user
    @PostMapping
    public ResponseEntity<DepositResponse> deposit(@RequestBody DepositRequest request) {
        return ResponseEntity.ok(depositService.deposit(request));
    }
}

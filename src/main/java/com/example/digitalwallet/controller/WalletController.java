package com.example.digitalwallet.controller;

import com.example.digitalwallet.model.request.CreateWalletRequest;
import com.example.digitalwallet.model.response.WalletResponse;
import com.example.digitalwallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // Her iki tür de girecek
    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody CreateWalletRequest request) {
        return ResponseEntity.ok(walletService.createWallet(userDetails.getUsername(), request));
    }

    // Her iki tür de girecek
    @GetMapping("/{customerId}")
    public ResponseEntity<List<WalletResponse>> getWalletListByCustomerId(@PathVariable String customerId,
                                                                          @RequestParam(required = false) String currency,
                                                                          @RequestParam(required = false) BigDecimal minBalance,
                                                                          @RequestParam(required = false) BigDecimal maxBalance) {
        return ResponseEntity.ok(walletService.getWalletListByCustomerId(customerId, currency, minBalance, maxBalance));
    }
}

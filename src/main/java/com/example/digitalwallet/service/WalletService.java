package com.example.digitalwallet.service;

import com.example.digitalwallet.converter.WalletConverter;
import com.example.digitalwallet.entity.Customer;
import com.example.digitalwallet.entity.Wallet;
import com.example.digitalwallet.model.request.CreateWalletRequest;
import com.example.digitalwallet.model.response.WalletResponse;
import com.example.digitalwallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletConverter walletConverter;
    private final CustomerDetailsService customerDetailsService;

    public WalletService(WalletRepository walletRepository, WalletConverter walletConverter, CustomerDetailsService customerDetailsService) {
        this.walletRepository = walletRepository;
        this.walletConverter = walletConverter;
        this.customerDetailsService = customerDetailsService;
    }

    public Wallet getWalletById(String walletId) {
        return walletRepository.findByWalletId(walletId);
    }

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public WalletResponse createWallet(String customerEmail, CreateWalletRequest request) {
        Customer customer = customerDetailsService.findByEmail(customerEmail);
        Wallet wallet = walletConverter.apply(customer.getCustomerId(), request);
        walletRepository.save(wallet);
        return walletConverter.apply(wallet);
    }

    public List<WalletResponse> getWalletListByCustomerId(String customerId, String currency, BigDecimal minBalance, BigDecimal maxBalance) {
        return walletRepository.findAllByCustomerId(customerId).stream()
                .filter(wallet -> currency == null || wallet.getCurrency().equalsIgnoreCase(currency))
                .filter(wallet -> minBalance == null || wallet.getBalance().compareTo(minBalance) >= 0)
                .filter(wallet -> maxBalance == null || wallet.getBalance().compareTo(maxBalance) <= 0)
                .map(walletConverter::apply)
                .collect(Collectors.toList());
    }


}

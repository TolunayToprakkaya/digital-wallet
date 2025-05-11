package com.example.digitalwallet.repository;

import com.example.digitalwallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    List<Wallet> findAllByCustomerId(String customerId);

    Wallet findByWalletId(String walletId);
}

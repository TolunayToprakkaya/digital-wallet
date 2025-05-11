package com.example.digitalwallet.repository;

import com.example.digitalwallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByWalletId(String walletId);
}

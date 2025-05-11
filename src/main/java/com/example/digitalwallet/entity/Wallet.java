package com.example.digitalwallet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tblWallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String walletId;
    private String customerId;
    private String name;
    private String currency;
    private boolean isActiveForShopping;
    private boolean isActiveForWithdraw;
    @Column(precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;
    @Column(precision = 19, scale = 4)
    private BigDecimal usableBalance = BigDecimal.ZERO;
}

package com.example.digitalwallet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tblTransaction")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String walletId;
    private BigDecimal amount;
    private String type;
    private String oppositePartyType;
    private String oppositeParty;
    private String status;
}

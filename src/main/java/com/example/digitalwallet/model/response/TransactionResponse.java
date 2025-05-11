package com.example.digitalwallet.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Integer id;
    private String walletId;
    private BigDecimal amount;
    private String type;
    private String oppositePartyType;
    private String oppositeParty;
    private String status;
}

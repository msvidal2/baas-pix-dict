package com.picpay.banking.pix.core.domain;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PixKey {

    private KeyType type;
    private String key;
    private Integer ispb;
    private String nameIspb;
    private String branchNumber;
    private AccountType accountType;
    private String accountNumber;
    private LocalDateTime accountOpeningDate;
    private PersonType personType;
    private String taxId;
    private String name;
    private String fantasyName;
    private LocalDateTime createdAt;
    private LocalDateTime startPossessionAt;
    private String endToEndId;
    private ClaimType claim;
    private Statistic statistic;

    public PixKey(String key, String name, String fantasyName, LocalDateTime createdAt, LocalDateTime startPossessionAt, ClaimType claim) {
        this.key = key;
        this.name = name;
        this.fantasyName = fantasyName;
        this.createdAt = createdAt;
        this.startPossessionAt = startPossessionAt;
        this.claim = claim;
    }
}

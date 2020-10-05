package com.picpay.banking.pix.core.domain;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DonorData {

    private String branchNumber;
    private AccountType accountType;
    private String accountNumber;
    private PersonType personType;
    private long cpfCnpj;
    private String name;
    private String fantasyName;
    private LocalDateTime notificationDate;
}

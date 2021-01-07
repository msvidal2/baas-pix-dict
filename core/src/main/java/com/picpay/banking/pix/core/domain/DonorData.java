package com.picpay.banking.pix.core.domain;

import com.google.common.base.Strings;
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

    public String getCpfCnpjWithLeftZeros() {
        int size = 11;

        if(PersonType.LEGAL_ENTITY.equals(personType)) {
            size = 14;
        }

        return Strings.padStart(String.valueOf(cpfCnpj), size, '0');
    }
}

package com.picpay.banking.pix.core.domain;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DonorData {

    public static final int CPF_SIZE = 11;
    public static final int CNPJ_SIZE = 14;

    private String branchNumber;
    private AccountType accountType;
    private String accountNumber;
    private PersonType personType;
    private long cpfCnpj;
    private String name;
    private String fantasyName;
    private LocalDateTime notificationDate;

    public String getCpfCnpjWithLeftZeros() {
        int size = CPF_SIZE;

        if(PersonType.LEGAL_ENTITY == personType) {
            size = CNPJ_SIZE;
        }

        return Strings.padStart(String.valueOf(cpfCnpj), size, '0');
    }
}

package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.original.dto.AccountTypeOriginal;
import com.picpay.banking.pix.original.dto.KeyTypeOriginal;
import com.picpay.banking.pix.original.dto.PersonTypeOriginal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListPixKeyResponseDTO {

    private String keyCod;
    private KeyTypeOriginal keyType;
    private String ispb;
    private String branch;
    private String account;
    private String taxId;
    private AccountTypeOriginal accountType;
    private String accountOpeningDate;
    private PersonTypeOriginal typePerson;
    private String name;
    private String creationDate;

    public PixKey toDomain() {
        return PixKey.builder()
            .key(keyCod)
            .type(keyType.getKeyType())
            .branchNumber(branch)
            .accountNumber(account)
            .accountType(accountType.getAccountTypeDomain())
            .taxId(taxId)
            .personType(typePerson.getPersonType())
            .name(name)
            .accountOpeningDate(LocalDateTime.parse(accountOpeningDate,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")))
            .createdAt(LocalDateTime.parse(creationDate,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")))
            .ispb(Integer.valueOf(ispb))
            .build();
    }
}
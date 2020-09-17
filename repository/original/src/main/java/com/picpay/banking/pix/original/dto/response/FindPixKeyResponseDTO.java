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

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindPixKeyResponseDTO {

    private String account;
    private LocalDateTime accountOpeningDate;
    private AccountTypeOriginal accountType;
    private String bankName;
    private String branch;
    private String businessPerson;
    private LocalDateTime creationDate;
    private String ispb;
    private LocalDateTime keyOwnershipDate;
    private KeyTypeOriginal keyType;
    private String name;
    private String returnMessage;
    private String taxId;
    private String taxIdMask;
    private PersonTypeOriginal typePerson;
    private LocalDateTime openClaimCreationDate;

    public PixKey toDomain() {
        return PixKey.builder()
                .type(keyType.getKeyType())
                .ispb(Integer.valueOf(ispb))
                .branchNumber(branch)
                .accountNumber(account)
                .accountType(accountType.getAccountTypeDomain())
                .accountOpeningDate(accountOpeningDate)
                .taxId(taxId)
                .personType(typePerson.getPersonType())
                .name(name)
                .fantasyName(businessPerson)
                .createdAt(creationDate)
                .build();
    }

}

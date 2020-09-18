package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListPixKeyResponseDTO {

    private String account;
    private LocalDateTime accountOpeningDate;
    private String accountType;
    private String bankName;
    private String branch;
    private String businessPerson;
    private LocalDateTime creationDate;
    private String ispb;
    private LocalDateTime keyOwnershipDate;
    private String keyType;
    private String name;
    private String returnMessage;
    private String taxId;
    private String taxIdMask;
    private String typePerson;
    private LocalDateTime openClaimCreationDate;

    public PixKey toDomain() {
        return PixKey.builder()
            .ispb(Integer.valueOf(ispb))
            .branchNumber(branch)
            .accountNumber(account)
            .startPossessionAt(keyOwnershipDate)
            .accountOpeningDate(accountOpeningDate)
            .taxId(taxId)
            .name(name)
            .fantasyName(businessPerson)
            .createdAt(creationDate)
            .build();
    }
}
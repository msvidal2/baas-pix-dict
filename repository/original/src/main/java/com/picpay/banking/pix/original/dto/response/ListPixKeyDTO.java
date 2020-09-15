package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class ListPixKeyDTO {

    private String keyCod;
    private String keyType;
    private String ispb;
    private String bankName;
    private String branch;
    private String account;
    private String taxId;
    private String accountType;
    private LocalDateTime accountOpeningDate;
    private String typePerson;
    private String name;
    private String businessPerson;
    private LocalDateTime creationDate;
    private LocalDateTime keyOwnershipDate;

    public PixKey toDomain() {
        return PixKey.builder()
                .key(keyCod)
                .ispb(Integer.valueOf(ispb))
                .name(name)
                .fantasyName(name)
                .accountNumber(account)
                .taxId(taxId)
                .accountOpeningDate(accountOpeningDate)
                .startPossessionAt(creationDate)
                .build();
    }

}
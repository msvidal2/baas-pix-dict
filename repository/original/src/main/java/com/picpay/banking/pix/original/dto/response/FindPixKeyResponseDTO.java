package com.picpay.banking.pix.original.dto.response;

import com.picpay.banking.pix.core.domain.PixKey;
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
    private String accountOpeningDate;
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
        // TODO: implementar
        return PixKey.builder()
                .build();
    }

}

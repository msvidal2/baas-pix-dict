package com.picpay.banking.claim.dto.request;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pixkey.dto.request.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Claimer {

    private final OwnerType type;
    private final String taxIdNumber;
    private final String name;

    public static Claimer from(Claim claim) {
        return Claimer.builder()
                .type(OwnerType.resolve(claim.getPersonType()))
                .taxIdNumber(claim.getCpfCnpj())
                .name(claim.getOwnerName()) //TODO: checar isso aqui
                .build();
    }

}

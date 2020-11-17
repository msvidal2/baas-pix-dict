package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Owner {

    private OwnerType type;
    private String taxIdNumber;
    private String name;

    public static Owner from(PixKey pixKey) {
        return Owner.builder()
                .type(OwnerType.resolve(pixKey.getPersonType()))
                .taxIdNumber(pixKey.getTaxId())
                .name(pixKey.getOwnerName())
                .build();
    }

}
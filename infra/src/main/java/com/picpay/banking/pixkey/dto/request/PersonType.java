package com.picpay.banking.pixkey.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PersonType {

    NATURAL_PERSON(com.picpay.banking.pix.core.domain.PersonType.INDIVIDUAL_PERSON),
    LEGAL_PERSON(com.picpay.banking.pix.core.domain.PersonType.LEGAL_ENTITY);

    private com.picpay.banking.pix.core.domain.PersonType personType;

    public static PersonType resolve(com.picpay.banking.pix.core.domain.PersonType personType) {
        return Arrays.stream(PersonType.values())
                .filter(ownerType -> ownerType.personType.equals(personType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}
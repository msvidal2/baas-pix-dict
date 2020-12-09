package com.picpay.banking.pixkey.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PersonType {

    NATURAL_PERSON(com.picpay.banking.pix.core.domain.PersonType.INDIVIDUAL_PERSON, 0),
    LEGAL_PERSON(com.picpay.banking.pix.core.domain.PersonType.LEGAL_ENTITY, 1);

    private com.picpay.banking.pix.core.domain.PersonType personType;

    private int value;

    public static PersonType resolve(com.picpay.banking.pix.core.domain.PersonType personType) {
        return Arrays.stream(PersonType.values())
                .filter(ownerType -> ownerType.personType.equals(personType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}
package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.PersonType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OwnerType {

    NATURAL_PERSON(PersonType.INDIVIDUAL_PERSON),
    LEGAL_PERSON(PersonType.LEGAL_ENTITY);

    private PersonType personType;

    public static OwnerType resolve(PersonType personType) {
        return Arrays.stream(OwnerType.values())
                .filter(ownerType -> ownerType.personType.equals(personType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}
package com.picpay.banking.pix.original.dto;

import com.picpay.banking.pix.core.domain.PersonType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum PersonTypeOriginal {

    NATURAL_PERSON(PersonType.INDIVIDUAL_PERSON),
    LEGAL_PERSON(PersonType.LEGAL_ENTITY);

    private PersonType personType;

    public static PersonTypeOriginal resolveFromDomain(PersonType personType) {
        return Stream.of(values())
                .filter(v -> v.personType.equals(personType))
                .findAny()
                .orElse(null);
    }

}

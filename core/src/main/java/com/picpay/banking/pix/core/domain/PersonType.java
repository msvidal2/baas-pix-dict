package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonType {

    INDIVIDUAL_PERSON(0),
    LEGAL_ENTITY(1);

    private int value;

    public static PersonType resolve(int value) {
        for(PersonType personType : values()) {
            if (personType.value == value) {
                return personType;
            }
        }

        return null;
    }

}

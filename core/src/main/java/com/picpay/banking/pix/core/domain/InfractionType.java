package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfractionType {

    FRAUD ("0"),
    PLD_FT ("1");


    private String value;

    public static InfractionType resolve(String value) {
        for(InfractionType infractionType : values()) {
            if (infractionType.value.equalsIgnoreCase(value)) {
                return infractionType;
            }
        }
        throw new IllegalArgumentException("InfractionType can't be resolved, invalid value");
    }
}

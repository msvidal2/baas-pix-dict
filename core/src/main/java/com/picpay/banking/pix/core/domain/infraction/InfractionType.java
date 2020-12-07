package com.picpay.banking.pix.core.domain.infraction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum InfractionType implements Serializable {

    FRAUD (0),
    PLD_FT (1);

    private int value;

    public static InfractionType resolve(int value) {
        for(InfractionType infractionType : values()) {
            if (infractionType.value == value) {
                return infractionType;
            }
        }
        throw new IllegalArgumentException("InfractionType can't be resolved, invalid value");
    }
}

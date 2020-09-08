package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimType {

    PORTABILITY(0),
    POSSESION_CLAIM(1);

    private int value;

    public static ClaimType resolve(int value) {
        for(ClaimType claimType : values()) {
            if (claimType.value == value) {
                return claimType;
            }
        }

        return null;
    }

}

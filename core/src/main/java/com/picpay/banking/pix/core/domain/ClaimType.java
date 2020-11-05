package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimType {

    PORTABILITY(0),
    POSSESSION_CLAIM(1);

    private int value;

    public static ClaimType resolve(Integer value) {
        for(ClaimType claimType : values()) {
            if (value != null && claimType.value == value.intValue()) {
                return claimType;
            }
        }

        return null;
    }

}

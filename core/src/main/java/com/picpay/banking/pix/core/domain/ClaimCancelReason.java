package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  ClaimCancelReason {

    CLIENT_REQUEST(0),
    ACCOUNT_CLOSURE(1),
    FRAUD(4);

    private int value;

    public static ClaimCancelReason resolve(Integer value) {
        for(ClaimCancelReason claimCancelReason : values()) {
            if (value != null && claimCancelReason.value == value.intValue()) {
                return claimCancelReason;
            }
        }

        return null;
    }

}

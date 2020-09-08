package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimConfirmationReason {

    CLIENT_REQUEST(0),
    ACCOUNT_CLOSURE(1);

    private int value;

    public static ClaimConfirmationReason resolve(int value) {
        for(ClaimConfirmationReason reason : values()) {
            if (reason.value == value) {
                return reason;
            }
        }

        return null;
    }

}

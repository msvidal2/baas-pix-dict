package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimConfirmationReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST.getValue()),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE.getValue());

    private int value;

    public static ClaimConfirmationReason resolve(Integer value) {
        for(ClaimConfirmationReason reason : values()) {
            if (value != null && reason.value == value.intValue()) {
                return reason;
            }
        }

        return null;
    }

}

package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  ClaimCancelReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST.getValue()),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE.getValue()),
    FRAUD(Reason.FRAUD.getValue()),
    DEFAULT_RESPONSE(Reason.DEFAULT_RESPONSE.getValue());

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

package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum ClaimConfirmationReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST.getValue()),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE.getValue()),
    DEFAULT_RESPONSE(Reason.DEFAULT_RESPONSE.getValue());

    private int value;

    public static ClaimConfirmationReason resolve(int value) {
        for(ClaimConfirmationReason reason : values()) {
            if (reason.value == value) {
                return reason;
            }
        }

        return null;
    }

    public static List<ClaimConfirmationReason> portabilityConfirmReasons() {
        return List.of(CLIENT_REQUEST, ACCOUNT_CLOSURE);
    }

    public static List<ClaimConfirmationReason> ownershipConfirmReasons() {
        return List.of(CLIENT_REQUEST, DEFAULT_RESPONSE);
    }

}

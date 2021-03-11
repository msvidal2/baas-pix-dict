package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ClaimConfirmationReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE),
    DEFAULT_RESPONSE(Reason.DEFAULT_RESPONSE);

    private Reason value;

    public static ClaimConfirmationReason resolve(Reason value) {
        return Stream.of(values())
                .filter(r -> r.value.equals(value))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static List<ClaimConfirmationReason> portabilityConfirmReasons() {
        return List.of(CLIENT_REQUEST, ACCOUNT_CLOSURE);
    }

    public static List<ClaimConfirmationReason> ownershipConfirmReasons() {
        return List.of(CLIENT_REQUEST, DEFAULT_RESPONSE);
    }

}

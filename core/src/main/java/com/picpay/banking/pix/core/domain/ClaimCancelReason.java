package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.picpay.banking.pix.core.domain.ClaimantType.CLAIMANT;
import static com.picpay.banking.pix.core.domain.ClaimantType.DONOR;

@Getter
@AllArgsConstructor
public enum ClaimCancelReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE),
    FRAUD(Reason.FRAUD),
    DEFAULT_RESPONSE(Reason.DEFAULT_RESPONSE);

    private final Reason value;

    public static ClaimCancelReason resolve(Reason value) {
        return Arrays.stream(values())
                .filter(reason -> reason.value.equals(value))
                .findAny()
                .orElse(null);
    }

    public static Map<ClaimCancelReason, List<ClaimantType>> getOwnershipAllowedReasons() {
        return Map.of(
                CLIENT_REQUEST, List.of(CLAIMANT),
                ACCOUNT_CLOSURE, List.of(CLAIMANT),
                FRAUD, List.of(DONOR, CLAIMANT),
                DEFAULT_RESPONSE, List.of(CLAIMANT));
    }


    public static Map<ClaimCancelReason, List<ClaimantType>> getPortabilityAllowedReasons() {
        return Map.of(
                CLIENT_REQUEST, List.of(DONOR, CLAIMANT),
                ACCOUNT_CLOSURE, List.of(CLAIMANT),
                FRAUD, List.of(DONOR, CLAIMANT),
                DEFAULT_RESPONSE, List.of(DONOR));
    }

}

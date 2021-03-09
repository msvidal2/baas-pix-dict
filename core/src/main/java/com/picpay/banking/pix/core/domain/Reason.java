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
public enum Reason {

    CLIENT_REQUEST(0),
    ACCOUNT_CLOSURE(1),
    BRANCH_TRANSFER(2),
    INACTIVITY(3),
    FRAUD(4),
    DEFAULT_RESPONSE(5),
    RECONCILIATION(-1);

    private Integer value;

    public static Reason resolve(int value) {
        return Arrays.stream(Reason.values())
                .filter(reason -> reason.value.equals(value))
                .findFirst()
                .orElse(null);
    }

    public static Map<Reason, List<ClaimantType>> getOwnershipAllowedCancellationReasons() {
        return Map.of(
                CLIENT_REQUEST, List.of(CLAIMANT),
                ACCOUNT_CLOSURE, List.of(CLAIMANT),
                FRAUD, List.of(DONOR, CLAIMANT),
                DEFAULT_RESPONSE, List.of(CLAIMANT));
    }


    public static Map<Reason, List<ClaimantType>> getPortabilityAllowedCancellationReasons() {
        return Map.of(
                CLIENT_REQUEST, List.of(DONOR, CLAIMANT),
                ACCOUNT_CLOSURE, List.of(CLAIMANT),
                FRAUD, List.of(DONOR, CLAIMANT),
                DEFAULT_RESPONSE, List.of(DONOR));
    }

}

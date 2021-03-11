package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import static com.picpay.banking.pix.core.domain.ClaimantType.CLAIMANT;
import static com.picpay.banking.pix.core.domain.ClaimantType.DONOR;

@Getter
@AllArgsConstructor
public enum ClaimReason {

    CLIENT_REQUEST,
    ACCOUNT_CLOSURE,
    FRAUD,
    DEFAULT_OPERATION;

    /*
    CONFIRM REASON:
    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE),
    DEFAULT_RESPONSE(Reason.DEFAULT_RESPONSE);
     */

    /*
    CANCEL REASON
    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE),
    FRAUD(Reason.FRAUD),
    DEFAULT_RESPONSE(Reason.DEFAULT_RESPONSE);
     */

    public static List<ClaimReason> portabilityConfirmReasons() {
        return List.of(CLIENT_REQUEST, ACCOUNT_CLOSURE);
    }

    public static List<ClaimReason> ownershipConfirmReasons() {
        return List.of(CLIENT_REQUEST, DEFAULT_OPERATION);
    }

    public static Map<ClaimReason, List<ClaimantType>> getOwnershipAllowedCancelReasons() {
        return Map.of(
                CLIENT_REQUEST, List.of(CLAIMANT),
                ACCOUNT_CLOSURE, List.of(CLAIMANT),
                FRAUD, List.of(DONOR, CLAIMANT),
                DEFAULT_OPERATION, List.of(CLAIMANT));
    }


    public static Map<ClaimReason, List<ClaimantType>> getPortabilityAllowedCancelReasons() {
        return Map.of(
                CLIENT_REQUEST, List.of(DONOR, CLAIMANT),
                ACCOUNT_CLOSURE, List.of(CLAIMANT),
                FRAUD, List.of(DONOR, CLAIMANT),
                DEFAULT_OPERATION, List.of(DONOR));
    }

}

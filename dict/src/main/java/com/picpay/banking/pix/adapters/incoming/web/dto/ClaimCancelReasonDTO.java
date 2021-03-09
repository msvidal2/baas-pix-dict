package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.ClaimantType;
import com.picpay.banking.pix.core.domain.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.picpay.banking.pix.core.domain.ClaimantType.CLAIMANT;
import static com.picpay.banking.pix.core.domain.ClaimantType.DONOR;

@Getter
@AllArgsConstructor
public enum ClaimCancelReasonDTO {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE),
    FRAUD(Reason.FRAUD),
    DEFAULT_RESPONSE(Reason.DEFAULT_RESPONSE);

    private final Reason value;

    public static ClaimCancelReasonDTO resolve(Reason reason) {
        return Arrays.stream(values())
                .filter(localReason -> localReason.value.equals(reason))
                .findAny()
                .orElse(null);
    }

    public static Map<ClaimCancelReasonDTO, List<ClaimantType>> getOwnershipAllowedReasons() {
        return Map.of(
                CLIENT_REQUEST, List.of(CLAIMANT),
                ACCOUNT_CLOSURE, List.of(CLAIMANT),
                FRAUD, List.of(DONOR, CLAIMANT),
                DEFAULT_RESPONSE, List.of(CLAIMANT));
    }


    public static Map<ClaimCancelReasonDTO, List<ClaimantType>> getPortabilityAllowedReasons() {
        return Map.of(
                CLIENT_REQUEST, List.of(DONOR, CLAIMANT),
                ACCOUNT_CLOSURE, List.of(CLAIMANT),
                FRAUD, List.of(DONOR, CLAIMANT),
                DEFAULT_RESPONSE, List.of(DONOR));
    }

}

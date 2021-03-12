package com.picpay.banking.pix.adapters.incoming.web.dto.claim.request;

import com.picpay.banking.pix.core.domain.ClaimReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ClaimCancelReasonDTO {

    CLIENT_REQUEST(ClaimReason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(ClaimReason.ACCOUNT_CLOSURE),
    FRAUD(ClaimReason.FRAUD),
    DEFAULT_RESPONSE(ClaimReason.DEFAULT_OPERATION);

    private final ClaimReason claimReason;

    public static ClaimCancelReasonDTO resolve(ClaimReason cancelReason) {
        return Arrays.stream(values())
                .filter(reason -> reason.getClaimReason().equals(cancelReason))
                .findAny()
                .orElse(null);
    }

}

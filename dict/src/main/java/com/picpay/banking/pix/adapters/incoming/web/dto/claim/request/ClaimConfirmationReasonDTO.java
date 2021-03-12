package com.picpay.banking.pix.adapters.incoming.web.dto.claim.request;

import com.picpay.banking.pix.core.domain.ClaimReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ClaimConfirmationReasonDTO {

    CLIENT_REQUEST(ClaimReason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(ClaimReason.ACCOUNT_CLOSURE),
    DEFAULT_RESPONSE(ClaimReason.DEFAULT_OPERATION);

    private final ClaimReason claimReason;

    public static ClaimConfirmationReasonDTO resolve(ClaimReason confirmReason) {
        return Arrays.stream(values())
                .filter(reason -> reason.getClaimReason().equals(confirmReason))
                .findAny()
                .orElse(null);
    }

}

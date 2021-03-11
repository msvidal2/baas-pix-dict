package com.picpay.banking.claim.dto.request;

import com.picpay.banking.pix.core.domain.ClaimReason;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConfirmClaimReason {

    USER_REQUESTED(ClaimReason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(ClaimReason.ACCOUNT_CLOSURE),
    DEFAULT_OPERATION(ClaimReason.DEFAULT_OPERATION);

    private ClaimReason confirmationReason;

    public static ConfirmClaimReason resolve(ClaimReason claimConfirmationReason) {
        return Arrays.stream(ConfirmClaimReason.values())
                .filter(reason -> reason.confirmationReason.equals(claimConfirmationReason))
                .findAny()
                .orElse(null);
    }

}

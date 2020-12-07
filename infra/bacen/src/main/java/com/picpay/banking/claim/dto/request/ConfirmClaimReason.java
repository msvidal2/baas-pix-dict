package com.picpay.banking.claim.dto.request;

import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConfirmClaimReason {

    USER_REQUESTED(ClaimConfirmationReason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(ClaimConfirmationReason.ACCOUNT_CLOSURE),
    DEFAULT_OPERATION(ClaimConfirmationReason.DEFAULT_RESPONSE);

    private ClaimConfirmationReason confirmationReason;

    public static ConfirmClaimReason resolve(ClaimConfirmationReason claimConfirmationReason) {
        return Arrays.stream(ConfirmClaimReason.values())
                .filter(reason -> reason.confirmationReason.equals(claimConfirmationReason))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}

package com.picpay.banking.claim.dto;

import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ClaimReason {

    USER_REQUESTED(ClaimCancelReason.CLIENT_REQUEST, ClaimConfirmationReason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(ClaimCancelReason.ACCOUNT_CLOSURE, ClaimConfirmationReason.ACCOUNT_CLOSURE),
    FRAUD(ClaimCancelReason.FRAUD, null),
    DEFAULT_OPERATION(ClaimCancelReason.DEFAULT_RESPONSE, ClaimConfirmationReason.DEFAULT_RESPONSE);

    private ClaimCancelReason cancelReason;

    private ClaimConfirmationReason confirmationReason;

    public static ClaimReason from(ClaimCancelReason reason) {
        return Stream.of(values())
                .filter(v -> v.cancelReason.equals(reason))
                .findAny()
                .orElse(null);
    }

}

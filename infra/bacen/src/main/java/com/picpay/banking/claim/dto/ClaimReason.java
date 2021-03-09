package com.picpay.banking.claim.dto;

import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.domain.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ClaimReason {

    USER_REQUESTED(Reason.CLIENT_REQUEST, ClaimConfirmationReason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE, ClaimConfirmationReason.ACCOUNT_CLOSURE),
    FRAUD(Reason.FRAUD, null),
    DEFAULT_OPERATION(Reason.DEFAULT_RESPONSE, ClaimConfirmationReason.DEFAULT_RESPONSE);

    private Reason cancelReason;

    private ClaimConfirmationReason confirmationReason;

    public static ClaimReason from(Reason reason) {
        return Stream.of(values())
                .filter(v -> v.cancelReason.equals(reason))
                .findAny()
                .orElse(null);
    }

}

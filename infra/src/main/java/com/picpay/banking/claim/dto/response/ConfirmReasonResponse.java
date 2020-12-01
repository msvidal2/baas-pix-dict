package com.picpay.banking.claim.dto.response;

import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConfirmReasonResponse {

    USER_REQUESTED(ClaimConfirmationReason.CLIENT_REQUEST),
    ACCOUNT_CLOSURE(ClaimConfirmationReason.ACCOUNT_CLOSURE),
    DEFAULT_OPERATION(ClaimConfirmationReason.DEFAULT_RESPONSE);

    private ClaimConfirmationReason confirmationReason;

}

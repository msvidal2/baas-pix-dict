package com.picpay.banking.claim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfirmReason {

    USER_REQUESTED,
    ACCOUNT_CLOSURE,
    FRAUD,
    DEFAULT_OPERATION
}

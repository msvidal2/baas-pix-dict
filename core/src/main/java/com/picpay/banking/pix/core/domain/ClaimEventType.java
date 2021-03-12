package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimEventType {

    OPEN,
    AWAITING_CLAIM,
    CONFIRMED,
    CANCELED,
    COMPLETED,
    PENDING_OPEN,
    PENDING_AWAITING_CLAIM,
    PENDING_CONFIRMED,
    PENDING_CANCELED,
    PENDING_COMPLETED;

}
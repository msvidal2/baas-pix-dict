package com.picpay.banking.pix.core.domain;

public enum ClaimEvent {

    PENDING_OPEN,
    PENDING_AWAITING_CLAIM,
    PENDING_CONFIRM,
    PENDING_CANCEL,
    PENDING_COMPLETE;

}
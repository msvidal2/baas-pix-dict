package com.picpay.banking.pix.core.events;

public enum ClaimEventType {

    CREATE_PENDING,
    CANCEL_PENDING,
    CONFIRM_PENDING,
    COMPLETE_PENDING,

    CREATED_BACEN,
    CANCELED_BACEN,
    CONFIRMED_BACEN,
    COMPLETED_BACEN,
    FAILED_BACEN;

}
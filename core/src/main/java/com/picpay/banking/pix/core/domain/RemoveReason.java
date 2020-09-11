package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RemoveReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST.getValue()),
    INACTIVITY(Reason.INACTIVITY.getValue()),
    FRAUD(Reason.FRAUD.getValue());

    private int value;

}

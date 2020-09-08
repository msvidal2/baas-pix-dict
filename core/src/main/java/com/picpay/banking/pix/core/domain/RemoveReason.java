package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RemoveReason {

    CLIENT_REQUEST(0),
    INACTIVITY(3),
    FRAUD(4);

    private int value;

}

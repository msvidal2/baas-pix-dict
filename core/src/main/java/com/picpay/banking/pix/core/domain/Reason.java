package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Reason {

    CLIENT_REQUEST(0),
    ACCOUNT_CLOSURE(1),
    BRANCH_TRANSFER(2),
    INACTIVITY(3),
    FRAUD(4);

    private int value;

}

package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UpdateReason {

    CLIENT_REQUEST(0),
    BRANCH_TRANSFER(2);

    private int value;
}

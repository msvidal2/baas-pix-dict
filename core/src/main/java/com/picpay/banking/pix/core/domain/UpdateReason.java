package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UpdateReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST.getValue()),
    BRANCH_TRANSFER(Reason.BRANCH_TRANSFER.getValue()),
    RECONCILIATION(Reason.RECONCILIATION.getValue());

    private int value;
}

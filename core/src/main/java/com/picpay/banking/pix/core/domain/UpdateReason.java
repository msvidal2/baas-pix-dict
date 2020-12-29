package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UpdateReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    BRANCH_TRANSFER(Reason.BRANCH_TRANSFER),
    RECONCILIATION(Reason.RECONCILIATION);

    private Reason value;

}

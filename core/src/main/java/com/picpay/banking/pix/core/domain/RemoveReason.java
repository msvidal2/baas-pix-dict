package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RemoveReason {

    CLIENT_REQUEST(Reason.CLIENT_REQUEST),
    INACTIVITY(Reason.INACTIVITY),
    FRAUD(Reason.FRAUD),
    RECONCILIATION(Reason.RECONCILIATION);

    private Reason value;

}

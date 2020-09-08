package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountantType {

    DONE_TRANSACTIONS(0),
    FRAUDS_REPORT(1),
    PREVENTION_LAUNDERING(2),
    FRAUDS_CONFIRMATION(3),
    PREVENTION_LAUNDERING_CONFIRMATION(4);

    private int value;

}

package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AccountantType {

    DONE_TRANSACTIONS(0),
    FRAUDS_REPORT(1),
    PREVENTION_LAUNDERING(2),
    FRAUDS_CONFIRMATION(3),
    PREVENTION_LAUNDERING_CONFIRMATION(4);

    private Integer value;

    public static AccountantType resolve(int value) {
        return Arrays.stream(AccountantType.values())
                .filter(accountantType -> accountantType.value.equals(value))
                .findFirst()
                .orElse(null);
    }

}

package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Reason {

    CLIENT_REQUEST(0),
    ACCOUNT_CLOSURE(1),
    BRANCH_TRANSFER(2),
    INACTIVITY(3),
    FRAUD(4),
    DEFAULT_RESPONSE(5),
    RECONCILIATION(-1);

    private Integer value;

    public static Reason resolve(int value) {
        return Arrays.stream(Reason.values())
                .filter(reason -> reason.value.equals(value))
                .findFirst()
                .orElse(null);
    }

}

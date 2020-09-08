package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Aggregate {

    KEY(1),
    OWNER(2),
    ACCOUNT(3);

    private int value;

    public static Aggregate resolve(int value) {
        for(Aggregate aggregate : values()) {
            if (aggregate.value == value) {
                return aggregate;
            }
        }

        return null;
    }

}

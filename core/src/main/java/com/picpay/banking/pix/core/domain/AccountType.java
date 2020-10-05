package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {

    CHECKING(0),
    SALARY(1),
    SAVINGS(2);

    private int value;

    public static AccountType resolve(int value) {
        for(AccountType accountType : values()) {
            if (accountType.value == value) {
                return accountType;
            }
        }
        return null;
    }

}

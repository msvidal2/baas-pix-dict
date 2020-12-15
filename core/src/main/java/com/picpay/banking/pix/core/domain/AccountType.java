package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {

    CHECKING(0,"CACC"),
    SALARY(1,"SLRY"),
    SAVINGS(2,"SVGS");

    private int value;
    private String initials;

    public static AccountType resolve(int value) {
        for(AccountType accountType : values()) {
            if (accountType.value == value) {
                return accountType;
            }
        }
        return null;
    }

}

package com.picpay.banking.pixkey.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountType {

    CACC(com.picpay.banking.pix.core.domain.AccountType.CHECKING, 0),
    SLRY(com.picpay.banking.pix.core.domain.AccountType.SALARY, 1),
    SVGS(com.picpay.banking.pix.core.domain.AccountType.SAVINGS, 2);

    private com.picpay.banking.pix.core.domain.AccountType type;

    private int value;

    public static AccountType resolve(com.picpay.banking.pix.core.domain.AccountType accountType) {
        return Arrays.stream(AccountType.values())
                .filter(type -> type.type.equals(accountType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}

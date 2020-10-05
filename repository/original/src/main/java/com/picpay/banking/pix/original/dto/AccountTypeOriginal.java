package com.picpay.banking.pix.original.dto;

import com.picpay.banking.pix.core.domain.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum AccountTypeOriginal {

    CACC(AccountType.CHECKING),
    SLRY(AccountType.SALARY),
    SVGS(AccountType.SAVINGS);

    private AccountType accountTypeDomain;

    public static AccountTypeOriginal resolveFromDomain(AccountType accountTypeDomain) {
        return Stream.of(values())
                .filter(v -> v.accountTypeDomain.equals(accountTypeDomain))
                .findAny()
                .orElse(null);
    }

}

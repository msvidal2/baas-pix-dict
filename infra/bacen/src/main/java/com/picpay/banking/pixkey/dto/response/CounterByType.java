package com.picpay.banking.pixkey.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CounterByType {

    KEY(1),
    OWNER(2),
    ACCOUNT(3);

    private int value;

}

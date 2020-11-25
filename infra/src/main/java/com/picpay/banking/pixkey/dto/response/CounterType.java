package com.picpay.banking.pixkey.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CounterType {

    SETTLEMENTS(0),
    REPORTED_FRAUDS(1),
    REPORTED_AML_CFT(2),
    CONFIRMED_FRAUDS(3),
    CONFIRMED_AML_CFT(4);

    private int value;

}

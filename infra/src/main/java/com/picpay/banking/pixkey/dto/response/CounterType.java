package com.picpay.banking.pixkey.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CounterType {

    SETTLEMENTS,
    REPORTED_FRAUDS,
    REPORTED_AML_CFT,
    CONFIRMED_FRAUDS,
    CONFIRMED_AML_CFT;

}

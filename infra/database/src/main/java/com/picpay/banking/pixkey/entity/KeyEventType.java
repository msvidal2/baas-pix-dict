package com.picpay.banking.pixkey.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeyEventType {

    OPEN(0),
    CREATED(1),
    REMOVED(2);

    private final int value;

}

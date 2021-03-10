package com.picpay.banking.claim.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimEventType {

    OPEN(0),
    CREATED(1),
    REMOVED(2);

    private final int value;

}

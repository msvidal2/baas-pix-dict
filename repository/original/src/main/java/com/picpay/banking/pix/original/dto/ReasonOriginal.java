package com.picpay.banking.pix.original.dto;

import com.picpay.banking.pix.core.domain.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ReasonOriginal {

    USER_REQUESTED(Reason.CLIENT_REQUEST.getValue()),
    ACCOUNT_CLOSURE(Reason.ACCOUNT_CLOSURE.getValue()),
    BRANCH_TRANSFER(Reason.BRANCH_TRANSFER.getValue()),
    ENTRY_INACTIVITY(Reason.INACTIVITY.getValue()),
    FRAUD(Reason.FRAUD.getValue());

    private int value;

    public static ReasonOriginal resolve(int value) {
        return Stream.of(values())
                .filter(v -> v.value == value)
                .findAny()
                .orElse(null);
    }

}

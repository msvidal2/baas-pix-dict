package com.picpay.banking.pix.original.dto;

import com.picpay.banking.pix.core.domain.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ClaimReasonOriginal {

    USER_REQUESTED(Reason.CLIENT_REQUEST.getValue());

    private int value;

    public static ClaimReasonOriginal from(int value) {
        return Stream.of(values())
                .filter(v -> v.value == value)
                .findAny()
                .orElse(null);
    }

}

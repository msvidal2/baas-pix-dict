package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;

import java.util.stream.Stream;

@AllArgsConstructor
public enum ClaimantType {

    CLAIMANT(true), DONOR(false);

    private boolean value;

    public static ClaimantType resolve(final boolean value) {
        return Stream.of(values())
                .filter(e -> e.value == value)
                .findAny()
                .orElse(null);
    }

}

package com.picpay.banking.pix.core.domain.claim.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum AlertPersonType {

    REGULAR(0),
    LEGAL(1);

    private Integer value;

    public static AlertPersonType resolve(Integer value) {
        return Stream.of(values())
                .filter(enumType -> enumType.value.equals(value))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid Person Type"));
    }

}

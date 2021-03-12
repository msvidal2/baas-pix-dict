package com.picpay.banking.pixkey.entity;

import com.picpay.banking.pix.core.domain.PixKeySituation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Situation {

    OPEN(PixKeySituation.OPEN),
    ACTIVE(PixKeySituation.ACTIVE),
    INACTIVE(PixKeySituation.INACTIVE);

    private PixKeySituation value;

    public static Situation resolve(PixKeySituation situation) {
        return Stream.of(values())
                .filter(v -> v.value.equals(situation))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}

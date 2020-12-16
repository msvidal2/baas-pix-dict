package com.picpay.banking.pix.core.domain.infraction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum InfractionAnalyzeResult implements Serializable {

    ACCEPTED (0),
    REJECTED (1);

    private int value;

    public static InfractionAnalyzeResult resolve(int value) {
        for(InfractionAnalyzeResult result : values()) {
            if (result.value == value) {
                return result;
            }
        }
        return null;
    }


}

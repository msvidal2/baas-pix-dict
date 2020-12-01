package com.picpay.banking.pix.core.domain.infraction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfractionAnalyzeResult {

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

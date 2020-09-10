package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InfractionReportSituation {

    OPEN (0);

    private int value;

    public static InfractionReportSituation resolve(int value) {
        for(InfractionReportSituation infractionReportSituation : values()) {
            if (infractionReportSituation.value == value) {
                return infractionReportSituation;
            }
        }
        return null;
    }

}
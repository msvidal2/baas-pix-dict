package com.picpay.banking.pix.core.domain.infraction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum InfractionReportSituation implements Serializable {

    OPEN (0),
    RECEIVED (1),
    CANCELLED(2),
    ANALYZED (3);

    private Integer value;

    public static InfractionReportSituation resolve(Integer value) {
        for(InfractionReportSituation infractionReportSituation : values()) {
            if (infractionReportSituation.value.equals(value)) {
                return infractionReportSituation;
            }
        }
        return null;
    }

}
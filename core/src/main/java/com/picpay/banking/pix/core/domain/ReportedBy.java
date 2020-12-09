package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum ReportedBy implements Serializable {

    DEBITED_PARTICIPANT (0),
    CREDITED_PARTICIPANT (1);

    private int value;

    public static ReportedBy resolve(int value) {
        for(ReportedBy reportedBy : values()) {
            if (reportedBy.value == value) {
                return reportedBy;
            }
        }
        return null;
    }

}
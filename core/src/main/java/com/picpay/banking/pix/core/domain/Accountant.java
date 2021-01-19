package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class Accountant implements Serializable {

    private static final long serialVersionUID = 4425565430713016014L;

    private AccountantType type;
    private Aggregate aggregate;
    private int lastThreeDays;
    private int lastThirtyDays;
    private int lastSixMonths;

}

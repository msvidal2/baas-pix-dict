package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class Accountant {

    private AccountantType type;
    private Aggregate aggregate;
    private int lastThreeDays;
    private int lastThirtyDays;
    private int lastSixMonths;

}

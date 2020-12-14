package com.picpay.banking.pix.core.domain.claim.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlertAccountType {

    CHECKING_PAYMENT_ACCOUNT,
    SALARY_ACCOUNT,
    SAVINGS_ACCOUNT;

}

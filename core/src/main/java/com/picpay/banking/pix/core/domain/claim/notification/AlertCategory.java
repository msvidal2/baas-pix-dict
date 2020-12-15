package com.picpay.banking.pix.core.domain.claim.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlertCategory {

    PIX_KEY,
    CLAIM,
    PAYER,
    RECEIVER,
    ACCOUNT_MANAGEMENT,
    INFRACTION_REPORT;

}

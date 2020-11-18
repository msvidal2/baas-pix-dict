package com.picpay.banking.claim.dto.response;

import com.picpay.banking.pix.core.domain.ClaimSituation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ClaimStatus {

    OPEN(ClaimSituation.OPEN),
    WAITING_RESOLUTION(ClaimSituation.AWAITING_CLAIM),
    CONFIRMED(ClaimSituation.CONFIRMED),
    CANCELLED(ClaimSituation.CANCELED),
    COMPLETED(ClaimSituation.COMPLETED);

    private ClaimSituation claimSituation;
}
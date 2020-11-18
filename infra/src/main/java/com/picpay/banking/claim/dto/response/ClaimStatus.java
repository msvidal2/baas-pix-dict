package com.picpay.banking.claim.dto.response;

import com.picpay.banking.claim.dto.request.ClaimType;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ClaimStatus {

    OPEN(ClaimSituation.OPEN),
    WAITING_RESOLUTION(ClaimSituation.AWAITING_CLAIM),
    CONFIRMED(ClaimSituation.CONFIRMED),
    CANCELLED(ClaimSituation.CANCELED),
    COMPLETED(ClaimSituation.COMPLETED);

    private ClaimSituation claimSituation;

    public static ClaimStatus resolve(ClaimSituation claimSituation) {
        return Arrays.stream(ClaimStatus.values())
                .filter(status -> status.claimSituation.equals(claimSituation))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
    
}
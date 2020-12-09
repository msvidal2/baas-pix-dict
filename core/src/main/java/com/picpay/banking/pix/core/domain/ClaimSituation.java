package com.picpay.banking.pix.core.domain;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public enum ClaimSituation {

    OPEN(0),
    AWAITING_CLAIM(1),
    CONFIRMED(2),
    CANCELED(3),
    COMPLETED(4);

    private int value;

    public static ClaimSituation resolve(int value) {
        for(ClaimSituation claimSituation : values()) {
            if (claimSituation.value == value) {
                return claimSituation;
            }
        }

        return null;
    }

    public static List<ClaimSituation> getPending() {
        return List.of(OPEN, AWAITING_CLAIM, CONFIRMED);
    }

}

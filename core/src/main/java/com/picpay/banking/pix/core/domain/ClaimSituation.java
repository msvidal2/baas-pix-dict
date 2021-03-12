package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.picpay.banking.pix.core.domain.ClaimType.PORTABILITY;
import static com.picpay.banking.pix.core.domain.ClaimType.POSSESSION_CLAIM;

@Getter
@AllArgsConstructor
public enum ClaimSituation {

    OPEN(0),
    AWAITING_CLAIM(1),
    CONFIRMED(2),
    CANCELED(3),
    COMPLETED(4);

    private final int value;

    public static ClaimSituation resolve(int value) {
        return Arrays.stream(values())
                .filter(situation -> situation.value == value)
                .findAny()
                .orElse(null);
    }

    public static List<ClaimSituation> getPending() {
        return List.of(OPEN, AWAITING_CLAIM, CONFIRMED);
    }

    public static List<ClaimSituation> getNotPending() {
        return List.of(COMPLETED, CANCELED);
    }

    public static Map<ClaimType, List<ClaimSituation>> getCancelSituationsAllowedByType() {
        return Map.of(
                POSSESSION_CLAIM, List.of(AWAITING_CLAIM, CONFIRMED),
                PORTABILITY, List.of(AWAITING_CLAIM));
    }

}

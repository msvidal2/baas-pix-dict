package com.picpay.banking.claim.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ClaimType {

    OWNERSHIP(com.picpay.banking.pix.core.domain.ClaimType.POSSESSION_CLAIM),
    PORTABILITY(com.picpay.banking.pix.core.domain.ClaimType.PORTABILITY);

    private com.picpay.banking.pix.core.domain.ClaimType claimType;

    public static ClaimType resolve(com.picpay.banking.pix.core.domain.ClaimType claimType) {
        return Arrays.stream(ClaimType.values())
                .filter(type -> type.claimType.equals(claimType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}

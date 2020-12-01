package com.picpay.banking.claim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CancelledBy {

    CLAIMER(true),
    DONOR(false);

    private boolean value;

}

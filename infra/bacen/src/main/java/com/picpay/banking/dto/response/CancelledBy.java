package com.picpay.banking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CancelledBy {

    CLAIMER(true),
    DONOR(false);

    private boolean value;

}

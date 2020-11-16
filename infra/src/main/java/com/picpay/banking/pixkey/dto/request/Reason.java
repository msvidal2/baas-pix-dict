package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.CreateReason;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Reason {

    USER_REQUESTED(CreateReason.CLIENT_REQUEST),
    RECONCILIATION(null);

    private CreateReason createReason;

    public static Reason resolve(CreateReason reason) {
        return Arrays.stream(Reason.values())
                .filter(createReason -> createReason.createReason.equals(reason))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}

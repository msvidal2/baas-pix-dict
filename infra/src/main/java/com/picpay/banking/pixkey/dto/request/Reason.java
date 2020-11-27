package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.UpdateReason;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Reason {

    USER_REQUESTED(CreateReason.CLIENT_REQUEST, UpdateReason.CLIENT_REQUEST),
    BRANCH_TRANSFER(null, UpdateReason.BRANCH_TRANSFER),
    RECONCILIATION(null, null);

    private CreateReason createReason;
    private UpdateReason updateReason;

    public static Reason resolve(CreateReason reason) {
        return Arrays.stream(Reason.values())
                .filter(createReason -> createReason.createReason.equals(reason))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Reason resolve(UpdateReason reason) {
        return Arrays.stream(Reason.values())
                .filter(upReason -> upReason.updateReason.equals(reason))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}

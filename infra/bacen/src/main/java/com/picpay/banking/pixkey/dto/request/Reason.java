package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.domain.UpdateReason;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Reason {

    USER_REQUESTED(CreateReason.CLIENT_REQUEST, UpdateReason.CLIENT_REQUEST, RemoveReason.CLIENT_REQUEST),
    BRANCH_TRANSFER(null, UpdateReason.BRANCH_TRANSFER, null),
    RECONCILIATION(null, null, null),
    INACTIVITY(null, null, RemoveReason.INACTIVITY),
    FRAUD(null, null, RemoveReason.FRAUD);

    private CreateReason createReason;
    private UpdateReason updateReason;
    private RemoveReason removeReason;

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

    public static Reason resolve(RemoveReason reason) {
        return Arrays.stream(Reason.values())
                .filter(removeReason -> removeReason.removeReason.equals(reason))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}

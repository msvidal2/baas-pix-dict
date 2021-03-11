package com.picpay.banking.pixkey.dto.request;

import com.picpay.banking.pix.core.domain.Reason;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReasonDTO {

    USER_REQUESTED(Reason.CLIENT_REQUEST),
    BRANCH_TRANSFER(Reason.BRANCH_TRANSFER),
    RECONCILIATION(Reason.RECONCILIATION),
    INACTIVITY(Reason.INACTIVITY),
    FRAUD(Reason.FRAUD);

    private Reason reason;

    public static ReasonDTO resolve(Reason reason) {
        return Arrays.stream(ReasonDTO.values())
                .filter(createReason -> createReason.reason.equals(reason))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

}

package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode(of = {"cid", "action", "eventOnBacenAt"})
public class BacenCidEvent {

    @NonNull
    private final String cid;

    @NonNull
    private final ReconciliationAction action;

    @NonNull
    private final LocalDateTime eventOnBacenAt;

}

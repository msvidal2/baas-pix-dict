package com.picpay.banking.pix.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"cid", "action", "eventOnBacenAt"})
public class ReconciliationEvent {

    private final String key;

    private final KeyType keyType;

    @NonNull
    private final String cid;

    @NonNull
    private final ReconciliationAction action;

    @NonNull
    private final LocalDateTime eventOnBacenAt;

}

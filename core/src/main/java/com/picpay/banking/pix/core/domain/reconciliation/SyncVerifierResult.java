package com.picpay.banking.pix.core.domain.reconciliation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SyncVerifierResult {

    private final SyncVerifierResultType syncVerifierResultType;
    private final LocalDateTime syncVerifierLastModified;
    private final LocalDateTime responseTime;

}

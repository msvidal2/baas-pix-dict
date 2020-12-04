package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SyncVerifierResult {

    private final SyncVerifierResultType syncVerifierResultType;
    private final LocalDateTime syncVerifierLastModified;

}

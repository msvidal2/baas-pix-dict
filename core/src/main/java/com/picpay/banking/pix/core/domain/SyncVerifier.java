package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SyncVerifier {

    private final KeyType keyType;
    private String vsync;
    private LocalDateTime synchronizedAt;
    private SyncVerifierResult syncVerifierResult;

    public String calculateVsync(final List<ContentIdentifierEvent> contentIdentifierEvents) {
        if (vsync == null) vsync = "0000000000000000000000000000000000000000000000000000000000000000";

        BigInteger vsyncAsBigInteger = new BigInteger(vsync, 16);

        for (ContentIdentifierEvent event : contentIdentifierEvents) {
            BigInteger cidAsBigInteger = new BigInteger(event.getCid(), 16);
            vsyncAsBigInteger = vsyncAsBigInteger.xor(cidAsBigInteger);
        }

        return vsyncAsBigInteger.toString(16);
    }

    public SyncVerifierHistoric syncVerificationResult(final String vsyncCurrent, final SyncVerifierResult result) {
        final LocalDateTime synchronizedEnd = LocalDateTime.now();

        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .keyType(keyType)
            .vsyncStart(vsync)
            .vsyncEnd(vsyncCurrent)
            .synchronizedStart(synchronizedAt)
            .synchronizedEnd(synchronizedEnd)
            .syncVerifierResult(result)
            .build();

        this.syncVerifierResult = result;

        if (syncVerifierResult.equals(SyncVerifierResult.OK)) {
            this.vsync = vsyncCurrent;
            this.synchronizedAt = synchronizedEnd;
        }

        return syncVerifierHistoric;
    }

    public boolean isNOk() {
        return syncVerifierResult.equals(SyncVerifierResult.NOK);
    }

    public boolean isOK() {
        return syncVerifierResult.equals(SyncVerifierResult.OK);
    }

}

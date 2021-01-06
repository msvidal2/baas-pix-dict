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
    @Builder.Default
    private String vsync = "0000000000000000000000000000000000000000000000000000000000000000";
    private LocalDateTime synchronizedAt;
    private SyncVerifierResultType syncVerifierResultType;

    public String calculateVsync(final List<String> contentIdentifiers) {
        BigInteger vsyncAsBigInteger = new BigInteger(vsync, 16);

        for (String contentIdentifier : contentIdentifiers) {
            BigInteger cidAsBigInteger = new BigInteger(contentIdentifier, 16);
            vsyncAsBigInteger = vsyncAsBigInteger.xor(cidAsBigInteger);
        }

        return vsyncAsBigInteger.toString(16);
    }

    public SyncVerifierHistoric syncVerificationResult(final String vsyncCurrent, final SyncVerifierResult syncVerifierResult) {
        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .keyType(keyType)
            .vsyncStart(vsync)
            .vsyncEnd(vsyncCurrent)
            .synchronizedStart(synchronizedAt)
            .synchronizedEnd(syncVerifierResult.getSyncVerifierLastModified())
            .syncVerifierResultType(syncVerifierResult.getSyncVerifierResultType())
            .build();

        this.syncVerifierResultType = syncVerifierResult.getSyncVerifierResultType();

        if (syncVerifierResultType.equals(SyncVerifierResultType.OK)) {
            this.vsync = vsyncCurrent;
            this.synchronizedAt = syncVerifierResult.getSyncVerifierLastModified();
        }

        return syncVerifierHistoric;
    }

    public boolean isNOk() {
        return syncVerifierResultType.equals(SyncVerifierResultType.NOK);
    }

    public boolean isOK() {
        return syncVerifierResultType.equals(SyncVerifierResultType.OK);
    }

}

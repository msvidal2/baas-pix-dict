package com.picpay.banking.pix.core.domain.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SyncVerifier {

    private KeyType keyType;
    @Builder.Default
    private String vsync = "0000000000000000000000000000000000000000000000000000000000000000";
    private LocalDateTime synchronizedAt;
    private SyncVerifierResultType syncVerifierResultType;

    public String calculateVsync(final Stream<String> contentIdentifiers) {
       return contentIdentifiers.reduce(vsync, new VsyncBitwiseXOR());
    }

    public SyncVerifierHistoric syncVerificationResult(final String vsyncCurrent, final SyncVerifierResult syncVerifierResult) {
        var syncVerifierHistoric = SyncVerifierHistoric.builder()
            .keyType(keyType)
            .vsyncStart(vsync)
            .vsyncEnd(vsyncCurrent)
            .synchronizedStart(synchronizedAt)
            .synchronizedEnd(syncVerifierResult.getResponseTime())
            .syncVerifierResultType(syncVerifierResult.getSyncVerifierResultType())
            .build();

        this.syncVerifierResultType = syncVerifierResult.getSyncVerifierResultType();

        if (syncVerifierResultType == SyncVerifierResultType.OK) {
            this.vsync = vsyncCurrent;
            this.synchronizedAt = syncVerifierResult.getResponseTime();
        }

        return syncVerifierHistoric;
    }

    public boolean isNOk() {
        return syncVerifierResultType == SyncVerifierResultType.NOK;
    }

    public boolean isOK() {
        return syncVerifierResultType == SyncVerifierResultType.OK;
    }

    public static SyncVerifier defaultValue(KeyType keyType) {
        final int PIX_INITIAL_YEAR = 2020;
        return SyncVerifier.builder()
            .keyType(keyType)
            .synchronizedAt(LocalDateTime.of(PIX_INITIAL_YEAR, 1, 1, 0, 0))
            .build();
    }

}

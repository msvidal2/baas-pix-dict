package com.picpay.banking.pix.core.domain;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

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
        try {
            byte[] result = Hex.decodeHex(vsync);

            for (String contentIdentifier : contentIdentifiers) {
                result = xor(result, Hex.decodeHex(contentIdentifier));
            }

            return String.valueOf(Hex.encodeHex(result));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("The Cid must be a String that represents a hexadecimal with 64 characters.", e);
        }
    }

    private byte[] xor(byte[] a, byte[] b) {
        int length = Math.min(a.length, b.length);
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
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

        if (syncVerifierResultType.equals(SyncVerifierResultType.OK)) {
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

}

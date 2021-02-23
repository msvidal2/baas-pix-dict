package com.picpay.banking.pix.core.domain.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.time.LocalDateTime;

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

    public String calculateVsync(final Iterable<String> contentIdentifiers) {
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

    private static byte[] xor(byte[] a, byte[] b) {
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

}

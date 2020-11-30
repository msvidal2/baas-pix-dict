package com.picpay.banking.pix.sync;

import com.google.common.hash.Hashing;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Charsets.UTF_8;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReconciliationUtil {

    private static final String SEPARATOR = "&";

    public static String calculateCid(String keyType, String key, String ownerTaxIdNumber,
        String ownerName, String ownerTradeName, String participant, String branch, String accountNumber, String accountType,
        final String teste) {

        byte[] requestIdBytes = uuidAsBytes(UUID.fromString(teste));

        return Hashing.hmacSha256(requestIdBytes).newHasher()
            .putString(keyType, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(key, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(ownerTaxIdNumber, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(ownerName, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(ownerTradeName == null ? "" : ownerTradeName, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(participant, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(branch == null ? "" : branch, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(accountNumber, UTF_8).putString(SEPARATOR, UTF_8)
            .putString(accountType, UTF_8)
            .hash().toString().toLowerCase();

    }

    public static byte[] uuidAsBytes(UUID uuid) {
        byte[] bytes = new byte[16];
        ByteBuffer.wrap(bytes)
            .putLong(uuid.getMostSignificantBits())
            .putLong(uuid.getLeastSignificantBits());
        return bytes;
    }

    public static String calculateVsync(final String lastSyncVerifier, final Set<String> cids) {
        BigInteger sync = null;
        if (lastSyncVerifier != null) sync = new BigInteger(lastSyncVerifier, 16);

        for (String cid : cids) {
            BigInteger cidAsBigInteger = new BigInteger(cid, 16);
            if (sync == null) {
                sync = cidAsBigInteger;
                continue;
            }

            sync = sync.xor(cidAsBigInteger);
        }

        return sync.toString(16);
    }

}

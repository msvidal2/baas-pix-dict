package com.picpay.banking.pix.sync;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReconciliationUtil {

    public static String calculateCid(String keyType, String key, String ownerTaxIdNumber,
        String ownerName, String ownerTradeName, String participant, String branch, String accountNumber, String accountType) {

        StringBuilder entryAttributes = new StringBuilder();
        entryAttributes
            .append(keyType).append("&")
            .append(key).append("&")
            .append(ownerTaxIdNumber).append("&")
            .append(ownerName).append("&")
            .append(ownerTradeName).append("&")
            .append(participant).append("&")
            .append(branch).append("&")
            .append(accountNumber).append("&")
            .append(accountType);

        byte[] requestIdBytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(requestIdBytes, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(entryAttributes.toString().getBytes("UTF-8")));
        } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            log.error("Não foi possível obter o algorítimo HmacSHA256 para realizar o calculo do Cid para as chaves do Pix.", e);
        }

        return null;
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

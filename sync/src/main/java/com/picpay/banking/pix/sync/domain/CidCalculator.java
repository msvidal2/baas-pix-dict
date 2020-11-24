package com.picpay.banking.pix.sync.domain;

import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CidCalculator {

    public static String calculate(String keyType, String key, String ownerTaxIdNumber, String ownerName,
                                   String ownerTradeName, String participant, String branch, String accountNumber,
                                   String accountType) {

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

    private static final String PARTICIPANT_ID = "12345678";
    private static final String appender = "&";
    private final static byte[] requestIdBytes = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
    private final static String charset = "UTF-8";
    private final static String encryptFunction = "HmacSHA256";

    public static String calculateRf(PixKey pixKey) {

        StringBuilder entryAttributes = hashBuilder(pixKey);

        try {
            Mac sha256_Hmac = Mac.getInstance(encryptFunction);
            SecretKeySpec secret_key = new SecretKeySpec(requestIdBytes, encryptFunction);
            sha256_Hmac.init(secret_key);

            return Hex.encodeHexString(sha256_Hmac.doFinal(entryAttributes.toString().getBytes(charset)));
        } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            log.error("Não foi possível obter o algorítimo HmacSHA256 para realizar o calculo do Cid para as chaves do Pix.", e);   // TODO: refactor log to agree with the log pattern
            return null;
        }
    }

    private static StringBuilder hashBuilder(PixKey pixKey) {
        return new StringBuilder()
            .append(pixKey.getType()).append(appender)
            .append(pixKey.getKey()).append(appender)
            .append(pixKey.getTaxId()).append(appender)
            .append(pixKey.getOwnerName()).append(appender)
            .append(pixKey.getFantasyName()).append(appender)
            .append(PARTICIPANT_ID).append(appender)
            .append(pixKey.getBranchNumber()).append(appender)
            .append(pixKey.getAccountNumber()).append(appender)
            .append(pixKey.getAccountType());
    }

}

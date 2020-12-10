package com.picpay.banking.pix.core.domain;

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

    private static final String PARTICIPANT_ID = "12345678";    // TODO: refactor to get a env
    private static final String appender = "&";
    private final static String charset = "UTF-8";
    private final static String encryptFunction = "HmacSHA256";

    public static String calculate(ReconciliationEvent event) {

        try {
            StringBuilder entryAttributes = hashBuilder(event);

            Mac sha256_Hmac = Mac.getInstance(encryptFunction);
            SecretKeySpec secret_key = new SecretKeySpec(event.getRequestId().getBytes(), encryptFunction);
            sha256_Hmac.init(secret_key);

            return Hex.encodeHexString(sha256_Hmac.doFinal(entryAttributes.toString().getBytes(charset)));
        } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            log.error("Não foi possível obter o algorítimo HmacSHA256 para realizar o calculo do Cid para as chaves do Pix.", e);   // TODO: refactor log to agree with the log pattern
            return null;
        } catch (Exception e) {
            log.error("Fail to generate the CID hash");
            return null;
        }
    }

    private static StringBuilder hashBuilder(ReconciliationEvent event) throws Exception {
        return new StringBuilder()
            .append(event.getKeyType()).append(appender)
            .append(event.getKey()).append(appender)
            .append(event.getOwnerTaxIdNumber()).append(appender)
            .append(event.getOwnerName()).append(appender)
            .append(event.getOwnerTradeName()).append(appender)
            .append(PARTICIPANT_ID).append(appender)
            .append(event.getBranch()).append(appender)
            .append(event.getAccountNumber()).append(appender)
            .append(event.getAccountType());
    }
}

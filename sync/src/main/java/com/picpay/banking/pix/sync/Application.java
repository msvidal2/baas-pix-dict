package com.picpay.banking.pix.sync;

import com.picpay.banking.pix.core.usecase.reconciliation.CidProviderUseCase;
import com.picpay.banking.pix.sync.eventsourcing.CidProviderConsumer;
import com.picpay.banking.pix.sync.eventsourcing.EventSourceStream;
import org.apache.commons.codec.binary.Hex;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

@EnableBinding(value = {CidProviderConsumer.class, EventSourceStream.class})
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");

        String entryAttributes = "PHONE&+5511987654321&11122233300&João Silva&&12345678&00001&0007654321&CACC";
        String expectedCid = "28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88";

        //String cid = ReconciliationUtil.generateCid(entryAttributes);

//        System.out.println(cid);
//        System.out.println(cid.equals(expectedCid));


//        String[] cids = {"28c06eb41c4dc9c3ae114831efcac7446c8747777fca8b145ecd31ff8480ae88",
//            "4d4abb9168114e349672b934d16ed201a919cb49e28b7f66a240e62c92ee007f",
//            "fce514f84f37934bc8aa0f861e4f7392273d71b9d18e8209d21e4192a7842058"};
//
//        char[] vsync = new char[64];
//        for (int i = 0; i < 64; i++) {
//            vsync[i] = '0';
//        }
//
//        System.out.println(new String(vsync));
//
//        char[] calc = new char[64];
//        for (int i = 0; i < 64; i++) {
//            calc[i] = (char) (cids[0].charAt(i) ^ vsync[i]);
//        }
//
//        System.out.println(calc);






        //SpringApplication.run(Application.class, args);
    }

    public static String encode(byte[] key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    @Bean
    public static CidProviderUseCase cidProviderUseCase() {
        return new CidProviderUseCase();
    }

}

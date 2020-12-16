package com.picpay.banking.pix.dict.test.sync;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyncVerifierApplicationUtil {

    @SneakyThrows
    public void run(String keyType, boolean onlySyncVerifier) {
        List<String> envs = new ArrayList<>();
        envs.add("DB_URL=jdbc:postgresql://localhost:15432/postgres?currentSchema=dict");
        envs.add("DB_DRIVE_CLASS_NAME=org.postgresql.Driver");
        envs.add("DB_PASSWORD=123456");
        envs.add("DB_USERNAME=postgres");
        envs.add("DB_TYPE=POSTGRESQL");
        envs.add("PICPAY_REDIS_HOST=localhost");
        envs.add("PICPAY_REDIS_PORT=6379");
        envs.add("PICPAY_REDIS_TIMEOUT=5000");
        envs.add("PICPAY_REDIS_KEY_TTL=24");
        envs.add("PICPAY_LOGGING_LEVEL=INFO");
        envs.add("PICPAY_LOGGING_APPENDER=CONSOLE");
        envs.add("PARTNER=bacen");
        envs.add("BACEN_AWS_URL=http://baas-pix-proxy-bacen.ms.qa");
        envs.add("DB_SHOW_SQL=true");


        String[] envsAsArray = new String[envs.size()];
        for (int i = 0; i < envs.size(); i++) {
            envsAsArray[i] = envs.get(i);
        }

        String onlySyncVerifierParameter = onlySyncVerifier ? " -onlySyncVerifier" : "";

        Process process = Runtime.getRuntime().exec(
            "java -jar /Users/rodrigo.argentato/src/picpay/pix/baas-pix-dict/sync/sync-verifier/target/sync-verifier-1.0-SNAPSHOT.jar -keyType " + keyType + onlySyncVerifierParameter,
            envsAsArray);
        BufferedInputStream catOutput = new BufferedInputStream(process.getInputStream());
        byte[] contents = new byte[1024];

        int bytesRead;
        StringBuilder strFileContents = new StringBuilder();
        while ((bytesRead = catOutput.read(contents)) != -1) {
            strFileContents.append(new String(contents, 0, bytesRead));
        }

        System.out.print(strFileContents.toString());
    }

}

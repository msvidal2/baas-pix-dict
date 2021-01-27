package com.picpay.banking.pix.dict.test.sync;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyncVerifierApplicationUtil {

    @Value("${picpay.mysql.url}")
    private String mysqlUrl;

    @Value("${picpay.mysql.username}")
    private String mysqlUsername;

    @Value("${picpay.mysql.password}")
    private String mysqlpassword;

    @Value("${picpay.redis.host}")
    private String redisHost;

    @Value("${picpay.redis.port}")
    private String redisPort;

    @SneakyThrows
    public void run(String keyType, boolean onlySyncVerifier) {
        List<String> envs = new ArrayList<>();
        envs.add("DB_URL=" + mysqlUrl);
        envs.add("DB_DRIVE_CLASS_NAME=com.mysql.jdbc.Driver");
        envs.add("DB_PASSWORD=" + mysqlpassword);
        envs.add("DB_USERNAME=" + mysqlUsername);
        envs.add("DB_TYPE=MYSQL");
        envs.add("PICPAY_REDIS_HOST=" + redisHost);
        envs.add("PICPAY_REDIS_PORT=" + redisPort);
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
        String jarAbsolutePath = System.getProperty("user.dir") + "/.././sync/sync-verifier/target/sync-verifier-1.0-SNAPSHOT.jar";
        String jarCommand = "java -jar " + jarAbsolutePath + " -keyType " + keyType + onlySyncVerifierParameter;

        Process process = Runtime.getRuntime().exec(jarCommand, envsAsArray);
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

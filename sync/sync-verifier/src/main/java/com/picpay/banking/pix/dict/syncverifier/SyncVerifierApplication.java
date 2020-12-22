package com.picpay.banking.pix.dict.syncverifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class SyncVerifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncVerifierApplication.class, args);
    }

}
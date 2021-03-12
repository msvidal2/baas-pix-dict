package com.picpay.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients(basePackages = "com.picpay.banking.reconciliation.clients")
@SpringBootApplication(scanBasePackages = {"com.picpay.banking.common.*", "com.picpay.banking.config", "com.picpay.banking.reconciliation.*", "com.picpay.banking.pixkey.*", "com.picpay.banking.web"})
public class SyncVerifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncVerifierApplication.class, args);
    }

}
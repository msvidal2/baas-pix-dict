package com.picpay.banking.workers.pixkeydatabase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.picpay.banking.common.*",
    "com.picpay.banking.config",
    "com.picpay.banking.workers.pixkeydatabase.*",
    "com.picpay.banking.pixkey.*",
    "com.picpay.banking.reconciliation.*"})
public class PixKeyDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PixKeyDatabaseApplication.class, args);
    }

}

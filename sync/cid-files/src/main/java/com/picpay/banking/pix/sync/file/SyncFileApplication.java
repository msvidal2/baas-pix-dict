package com.picpay.banking.pix.sync.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Luis Silva
 * @version 1.0 10/12/2020
 */
@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class SyncFileApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SyncFileApplication.class, args);
        System.exit(SpringApplication.exit(applicationContext));
    }

}

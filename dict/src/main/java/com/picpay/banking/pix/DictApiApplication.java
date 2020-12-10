package com.picpay.banking.pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class DictApiApplication {

    public static void main(String[] args) {
        SpringApplication springApp = new SpringApplication(DictApiApplication.class);
        springApp.setAdditionalProfiles("database", "bacen");
        springApp.run(args);
    }
}

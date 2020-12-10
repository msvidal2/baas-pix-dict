package com.picpay.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class WorkerApplication {

    public static void main(String[] args) {
        SpringApplication springApp = new SpringApplication(WorkerApplication.class);
        springApp.setAdditionalProfiles("database", "messaging", "bacen");
        springApp.run(args);
    }

}

package com.picpay.banking.pix.dict.cidevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class CidEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CidEventsApplication.class, args);
    }

}

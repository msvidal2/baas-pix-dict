package com.picpay.banking.pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.picpay.banking.jdpi.clients")
@SpringBootApplication(scanBasePackages = "com.picpay.banking")
public class DictApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DictApiApplication.class, args);
    }
}

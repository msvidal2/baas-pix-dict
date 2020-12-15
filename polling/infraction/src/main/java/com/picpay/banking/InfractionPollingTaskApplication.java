package com.picpay.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableFeignClients(basePackages = "com.picpay.banking.infraction.clients")
@SpringBootApplication(scanBasePackages = { "com.picpay.banking.common.*", "com.picpay.banking.config", "com.picpay.banking.infraction.*"})
public class InfractionPollingTaskApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(InfractionPollingTaskApplication.class, args);
        System.exit(SpringApplication.exit(applicationContext));
    }

}

package com.picpay.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class BaasPixDictBacenInfractionTaskApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
            SpringApplication.run(BaasPixDictBacenInfractionTaskApplication.class, args);
        System.exit(SpringApplication.exit(applicationContext));
    }

}

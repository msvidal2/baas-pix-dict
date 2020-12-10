package com.picpay.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableFeignClients(basePackages = "com.picpay.banking.infraction.clients")
@SpringBootApplication(scanBasePackages = {"com.picpay.banking.config", "com.picpay.banking.infraction.*"})
public class BaasPixDictBacenInfractionTaskApplication {

    public static void main(String[] args) {
        SpringApplication springApp = new SpringApplication(BaasPixDictBacenInfractionTaskApplication.class);
        springApp.setAdditionalProfiles("database", "bacen");
        springApp.run(args);

//        ConfigurableApplicationContext applicationContext =
//            SpringApplication.run(BaasPixDictBacenInfractionTaskApplication.class, args);
//        System.exit(SpringApplication.exit(applicationContext));
    }

}

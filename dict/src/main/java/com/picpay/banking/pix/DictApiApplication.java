package com.picpay.banking.pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = {"com.picpay.banking.jdpi.clients", "com.picpay.banking.pixkey.clients"})
@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
@EnableJpaRepositories("com.picpay.banking.pixkey.repository")
@EntityScan("com.picpay.banking.pixkey.entity")
public class DictApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DictApiApplication.class, args);
    }
}

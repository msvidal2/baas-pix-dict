package com.picpay.banking.pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = {"com.picpay.banking.jdpi.clients", "com.picpay.banking.pixkey.clients", "com.picpay.banking.infraction.client"})
@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
@EnableJpaRepositories(basePackages = {"com.picpay.banking.pixkey.ports", "com.picpay.banking.infraction.ports.picpay"})
@EntityScan(basePackages = {"com.picpay.banking.pixkey.entity", "com.picpay.banking.infraction.entity"})
public class DictApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DictApiApplication.class, args);
    }
}

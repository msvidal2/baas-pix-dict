package com.picpay.banking;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.task.configuration.EnableTask;

@EnableFeignClients(basePackages = "com.picpay.banking.infraction.clients")
@SpringBootApplication(scanBasePackages = {"com.picpay.banking.config", "com.picpay.banking.infraction.*"})
@EnableTask
public class InfractionPollingTaskApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(InfractionPollingTaskApplication.class)
            .web(WebApplicationType.NONE)
            .run(args);
    }

}

package com.picpay.banking.claim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableFeignClients({
		"com.picpay.banking.claim.clients"
})
@SpringBootApplication(scanBasePackages = {
		"com.picpay.banking.claim",
		"com.picpay.banking.config",
		"com.picpay.banking.common"
})
public class PollingClaimApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(PollingClaimApplication.class, args);
		System.exit(SpringApplication.exit(applicationContext));
	}

}

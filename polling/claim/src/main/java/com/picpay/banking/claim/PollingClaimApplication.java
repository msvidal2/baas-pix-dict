package com.picpay.banking.claim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableFeignClients({
		"com.picpay.banking.claim.clients"
})
@SpringBootApplication(scanBasePackages = {
		"com.picpay.banking.claim",
		"com.picpay.banking.config"
})
public class PollingClaimApplication {

	public static void main(String[] args) {
		SpringApplication springApp = new SpringApplication(PollingClaimApplication.class);
		springApp.setAdditionalProfiles("database", "bacen");
		springApp.run(args);
	}

}

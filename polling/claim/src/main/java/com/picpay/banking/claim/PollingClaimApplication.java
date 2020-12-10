package com.picpay.banking.claim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients({
		"com.picpay.banking.pixkey.clients",
		"com.picpay.banking.claim.clients",
		"com.picpay.banking.infraction.clients",
		"com.picpay.banking.reconciliation.clients"
})
@SpringBootApplication(scanBasePackages = {
		"com.picpay.banking.claim",
		"com.picpay.banking.config"
})
public class PollingClaimApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollingClaimApplication.class, args);
	}

}

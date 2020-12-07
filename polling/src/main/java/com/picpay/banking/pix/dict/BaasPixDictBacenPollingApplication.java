package com.picpay.banking.pix.dict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients({
		"com.picpay.banking.pixkey.clients",
		"com.picpay.banking.claim.clients",
		"com.picpay.banking.infraction.clients",
		"com.picpay.banking.reconciliation.clients"
})
@SpringBootApplication(scanBasePackages = "com.picpay.banking")
public class BaasPixDictBacenPollingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaasPixDictBacenPollingApplication.class, args);
	}

}

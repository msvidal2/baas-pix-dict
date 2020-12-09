package com.picpay.banking.pix.dict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class BaasPixDictBacenInfractionTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaasPixDictBacenInfractionTaskApplication.class, args);
	}

}

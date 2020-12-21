package com.picpay.banking.pix.dict.cidevents;

import com.picpay.banking.pix.dict.cidevents.config.CidEventsInputBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
@EnableBinding(CidEventsInputBinding.class)
public class CidEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CidEventsApplication.class, args);
    }

}

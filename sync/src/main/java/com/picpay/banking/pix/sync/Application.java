package com.picpay.banking.pix.sync;

import com.picpay.banking.pix.core.usecase.reconciliation.CidProviderUseCase;
import com.picpay.banking.pix.sync.eventsourcing.CidProviderConsumer;
import com.picpay.banking.pix.sync.eventsourcing.TesteInputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

@EnableBinding(value = {CidProviderConsumer.class, TesteInputStream.class})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public static CidProviderUseCase cidProviderUseCase() {
        return new CidProviderUseCase();
    }

}

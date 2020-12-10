package com.picpay.banking.pix.dict.syncverifier.config;

import com.picpay.banking.pix.dict.syncverifier.service.SyncVerifierService;
import com.picpay.banking.pix.dict.syncverifier.task.SyncVerificationTask;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableTask
@Configuration
@RequiredArgsConstructor
public class TaskConfig {

    private final SyncVerifierService syncVerifierService;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new SyncVerificationTask(syncVerifierService);
    }

}

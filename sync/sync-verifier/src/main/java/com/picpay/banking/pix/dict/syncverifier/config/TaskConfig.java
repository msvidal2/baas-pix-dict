package com.picpay.banking.pix.dict.syncverifier.config;

import com.picpay.banking.pix.dict.syncverifier.service.SyncVerifierService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Configuration;

@EnableTask
@Configuration
@RequiredArgsConstructor
public class TaskConfig {

    private final SyncVerifierService syncVerifierService;

}

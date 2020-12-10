/*
 *  baas-pix-dict 1.0 12/9/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.claim.config;

import com.newrelic.api.agent.NewRelic;
import com.picpay.banking.claim.task.InfractionPollingTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.listener.annotation.FailedTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 09/12/2020
 */
@Configuration
@EnableTask
@Slf4j
public class TaskConfig {

    @Bean
    public CommandLineRunner commandLineRunner(final InfractionPollingTask infractionPollingTask) {
        return new InfractionCommandLineRunner(infractionPollingTask);
    }

    @BeforeTask
    public void beforeTask(TaskExecution taskExecution) {
        log.info("Starting bacen infraction polling");
    }

    @AfterTask
    public void afterTask(TaskExecution taskExecution) {
        log.info("Bacen infraction polling finished successfully");
        taskExecution.setExitMessage("SUCCESS");
    }

    @FailedTask
    public void onFailedTask(TaskExecution taskExecution, Throwable throwable) {
        NewRelic.noticeError(throwable);
        log.error("Bacen infraction polling task failed:", throwable);
        taskExecution.setExitCode(1);
        taskExecution.setExitMessage("FAILED");
    }

}

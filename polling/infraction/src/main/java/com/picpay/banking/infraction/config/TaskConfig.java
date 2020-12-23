/*
 *  baas-pix-dict 1.0 12/21/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.config;

import com.newrelic.api.agent.NewRelic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.listener.annotation.FailedTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 21/12/2020
 */
@Configuration
@Slf4j
@EnableTask
public class TaskConfig {

    @BeforeTask
    public void beforeTask(TaskExecution taskExecution) {
        log.info("Starting infraction polling task");
    }

    @AfterTask
    public void afterTask(TaskExecution taskExecution) {
        log.info("Infraction polling task was successful");
        taskExecution.setExitCode(0);
        taskExecution.setExitMessage("SUCCESS");
    }

    @FailedTask
    public void onFailedTask(TaskExecution taskExecution, Throwable throwable) {
        NewRelic.noticeError(throwable);
        log.error("Infraction polling failed. Cause: ", throwable);
        taskExecution.setExitCode(1);
        taskExecution.setExitMessage("FAILED");
    }

}

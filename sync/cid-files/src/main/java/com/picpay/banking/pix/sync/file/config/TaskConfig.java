package com.picpay.banking.pix.sync.file.config;

import com.newrelic.api.agent.NewRelic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.listener.annotation.FailedTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.context.annotation.Configuration;

/**
 * @author Luis Silva
 * @version 1.0 10/12/2020
 */
@Configuration
@EnableTask
@Slf4j
public class TaskConfig {

    @BeforeTask
    public void beforeTask(TaskExecution taskExecution) {
        log.info("Starting bacen sync file");
    }

    @AfterTask
    public void afterTask(TaskExecution taskExecution) {
        log.info("Bacen sync file finished successfully");
        taskExecution.setExitMessage("SUCCESS");
    }

    @FailedTask
    public void onFailedTask(TaskExecution taskExecution, Throwable throwable) {
        NewRelic.noticeError(throwable);
        log.error("Bacen sync file task failed:", throwable);
        taskExecution.setExitCode(1);
        taskExecution.setExitMessage("FAILED");
    }

}

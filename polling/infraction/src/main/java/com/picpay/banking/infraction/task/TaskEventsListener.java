/*
 *  baas-pix-dict 1.0 12/9/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.task;

import com.newrelic.api.agent.NewRelic;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.cloud.task.repository.TaskExecution;

/**
 * @author rafael.braga
 * @version 1.0 09/12/2020
 */
@Slf4j
public class TaskEventsListener implements TaskExecutionListener {

    @Override
    public void onTaskStartup(final TaskExecution taskExecution) {
        log.info("Starting bacen infraction polling");
    }

    @Override
    public void onTaskEnd(final TaskExecution taskExecution) {
        if (StringUtils.isBlank(taskExecution.getErrorMessage())) {
            taskExecution.setExitMessage("SUCCESS");
            log.info("Bacen infraction polling finished successfully");
            taskExecution.setExitCode(0);
        }
    }

    @Override
    public void onTaskFailed(final TaskExecution taskExecution, final Throwable throwable) {
        taskExecution.setExitMessage("FAILED");
        log.info("Bacen infraction polling FAILED!");
        NewRelic.noticeError(String.format("bacen_infraction_polling_failed: %s", taskExecution.getErrorMessage()));
    }

}

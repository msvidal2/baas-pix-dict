/*
 *  baas-pix-dict 1.0 12/10/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.config;

import com.picpay.banking.infraction.task.TaskEventsListener;
import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rafael.braga
 * @version 1.0 10/12/2020
 */
@Configuration
public class TaskBeanConfig {

    @Bean
    public TaskExecutionListener taskExecutionListener() {
        return new TaskEventsListener();
    }

}

/*
 *  baas-pix-dict 1.0 12/10/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.infraction.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 10/12/2020
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TaskApplicationRunner implements ApplicationRunner {

    private final InfractionPollingTask infractionPollingTask;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        log.info("Starting infractionPollingTask...");
        infractionPollingTask.run();
    }

}

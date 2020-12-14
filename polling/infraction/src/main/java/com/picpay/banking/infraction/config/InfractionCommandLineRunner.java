/*
 *  baas-pix-dict 1.0 12/9/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction.config;

import com.picpay.banking.infraction.task.InfractionPollingTask;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

/**
 * @author rafael.braga
 * @version 1.0 09/12/2020
 */
@RequiredArgsConstructor
public class InfractionCommandLineRunner implements CommandLineRunner {

    private final InfractionPollingTask infractionPollingTask;

    @Override
    public void run(String... strings) throws Exception {
        infractionPollingTask.run(null);
    }

}

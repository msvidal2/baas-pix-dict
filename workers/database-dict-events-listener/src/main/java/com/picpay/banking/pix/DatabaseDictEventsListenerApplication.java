/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class DatabaseDictEventsListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseDictEventsListenerApplication.class, args);
    }

}

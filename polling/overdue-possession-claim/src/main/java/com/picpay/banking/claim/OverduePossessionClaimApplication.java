/*
 *  baas-pix-dict 1.0 22/12/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.claim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author rafael.braga
 * @version 1.0 22/12/2020
 */
@EnableFeignClients({
    "com.picpay.banking.claim.clients"
})
@SpringBootApplication(scanBasePackages = {
    "com.picpay.banking.claim.*",
    "com.picpay.banking.config",
    "com.picpay.banking.common.*"
})
public class OverduePossessionClaimApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(OverduePossessionClaimApplication.class, args);
        System.exit(SpringApplication.exit(applicationContext));
    }

}

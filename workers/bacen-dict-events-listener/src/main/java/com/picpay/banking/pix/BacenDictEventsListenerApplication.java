/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@SpringBootApplication(scanBasePackages = "com.picpay.banking.*")
public class BacenDictEventsListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BacenDictEventsListenerApplication.class, args);
    }

}

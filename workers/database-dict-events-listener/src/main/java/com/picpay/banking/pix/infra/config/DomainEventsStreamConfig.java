/*
 *  baas-pix-dict 1.0 18/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.infra.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface DomainEventsStreamConfig {

    String DOMAIN_DATABASE_EVENTS = "domain-database-events";

    @Output(DOMAIN_DATABASE_EVENTS)
    MessageChannel output();

    @Input(DOMAIN_DATABASE_EVENTS)
    SubscribableChannel input();

}

/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.infra.config;

import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.EventKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Configuration
public class ProcessorBeanConfig {

    @Bean
    public Map<EventKey, EventProcessor> processors(final EventProcessor createInfractionOnBacenProcessor) {
        return Map.of(EventKey.builder().domain(Domain.INFRACTION_REPORT).eventType(EventType.INFRACTION_REPORT_CREATE_PENDING).build(), createInfractionOnBacenProcessor);
    }

}

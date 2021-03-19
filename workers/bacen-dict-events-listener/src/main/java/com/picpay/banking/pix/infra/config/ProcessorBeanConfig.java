/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.infra.config;

import com.picpay.banking.pix.core.events.EventKey;
import com.picpay.banking.pix.core.events.EventProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

import static com.picpay.banking.pix.core.events.Domain.INFRACTION_REPORT;
import static com.picpay.banking.pix.core.events.EventType.INFRACTION_REPORT_ANALYZE_PENDING;
import static com.picpay.banking.pix.core.events.EventType.INFRACTION_REPORT_CREATE_PENDING;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Configuration
public class ProcessorBeanConfig {

    @Bean
    public Map<EventKey, Optional<EventProcessor>> processors(final EventProcessor createInfractionOnBacenProcessor, EventProcessor analyzeInfractionOnBacenProcessor) {
        return Map.of(
            EventKey.builder().domain(INFRACTION_REPORT).eventType(INFRACTION_REPORT_CREATE_PENDING).build(), Optional.of(createInfractionOnBacenProcessor),
                EventKey.builder().domain(INFRACTION_REPORT).eventType(INFRACTION_REPORT_ANALYZE_PENDING).build(), Optional.of(analyzeInfractionOnBacenProcessor)
        );
    }

}

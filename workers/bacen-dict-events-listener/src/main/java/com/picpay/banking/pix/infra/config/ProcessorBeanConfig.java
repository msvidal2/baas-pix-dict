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
import static com.picpay.banking.pix.core.events.Domain.PIX_KEY;
import static com.picpay.banking.pix.core.events.EventType.*;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Configuration
public class ProcessorBeanConfig {

    @Bean
    public Map<EventKey, Optional<EventProcessor>> processors(final EventProcessor<?> createInfractionOnBacenProcessor,
                                                              final EventProcessor<?> createPixKeyDatabaseProcessor,
                                                              final EventProcessor<?> updatePixKeyDatabaseProcessor,
                                                              final EventProcessor<?> removePixKeyDatabaseProcessor,
                                                              final EventProcessor<?> updatePixKeyBacenProcessor) {
        return Map.of(
                // INFRACTION
                EventKey.builder().domain(INFRACTION_REPORT).eventType(INFRACTION_REPORT_CREATE_PENDING).build(),
                Optional.of(createInfractionOnBacenProcessor),
                // PIXKEY DATABASE
                EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_CREATED_BACEN).build(), Optional.of(createPixKeyDatabaseProcessor),
                EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_UPDATED_BACEN).build(), Optional.of(updatePixKeyDatabaseProcessor),
                EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_REMOVED_BACEN).build(), Optional.of(removePixKeyDatabaseProcessor),
                // PIXKEY BACEN
                EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_UPDATE_PENDING).build(), Optional.of(updatePixKeyBacenProcessor)
        );
    }

}
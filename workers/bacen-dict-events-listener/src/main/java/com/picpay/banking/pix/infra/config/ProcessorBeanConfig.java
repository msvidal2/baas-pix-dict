/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.infra.config;

import com.picpay.banking.pix.core.events.EventKey;
import com.picpay.banking.pix.core.events.EventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Optional;

import static com.picpay.banking.pix.core.events.Domain.*;
import static com.picpay.banking.pix.core.events.EventType.*;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Configuration
@RequiredArgsConstructor
public class ProcessorBeanConfig {

    @Bean
    public Map<EventKey, Optional<EventProcessor>> processors(
            final EventProcessor<?> createInfractionOnBacenProcessor,
            final EventProcessor<?> createPixKeyBacenProcessor,
            final EventProcessor<?> updatePixKeyBacenProcessor,
            final EventProcessor<?> removePixKeyBacenProcessor,
            final EventProcessor<?> analyzeInfractionOnBacenProcessor,
            final EventProcessor<?> createClaimBacenProcessor) {

        return Map.of(
                // INFRACTION
                EventKey.builder().domain(INFRACTION_REPORT).eventType(INFRACTION_REPORT_CREATE_PENDING).build(),Optional.of(createInfractionOnBacenProcessor),
                EventKey.builder().domain(INFRACTION_REPORT).eventType(INFRACTION_REPORT_ANALYZE_PENDING).build(), Optional.of(analyzeInfractionOnBacenProcessor),

                // PIXKEY BACEN
                EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_CREATE_PENDING).build(), Optional.of(createPixKeyBacenProcessor),
                EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_UPDATE_PENDING).build(), Optional.of(updatePixKeyBacenProcessor),
                EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_REMOVE_PENDING).build(), Optional.of(removePixKeyBacenProcessor),

                // CLAIM BACEN
                EventKey.builder().domain(CLAIM).eventType(CLAIM_CREATE_PENDING).build(), Optional.of(createClaimBacenProcessor)
        );
    }

}
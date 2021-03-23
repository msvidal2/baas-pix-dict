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

import static com.picpay.banking.pix.core.events.Domain.PIX_KEY;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_CREATED_BACEN;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_REMOVED_BACEN;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_UPDATED_BACEN;

@Configuration
@RequiredArgsConstructor
public class ProcessorBeanConfig {

    @Bean
    public Map<EventKey, Optional<EventProcessor>> processors(
        final EventProcessor<?> createPixKeyDatabaseProcessor,
        final EventProcessor<?> updatePixKeyDatabaseProcessor,
        final EventProcessor<?> removePixKeyDatabaseProcessor) {

        return Map.of(
            // PIXKEY DATABASE
            EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_CREATED_BACEN).build(), Optional.of(createPixKeyDatabaseProcessor),
            EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_UPDATED_BACEN).build(), Optional.of(updatePixKeyDatabaseProcessor),
            EventKey.builder().domain(PIX_KEY).eventType(PIX_KEY_REMOVED_BACEN).build(), Optional.of(removePixKeyDatabaseProcessor)
                     );
    }

}
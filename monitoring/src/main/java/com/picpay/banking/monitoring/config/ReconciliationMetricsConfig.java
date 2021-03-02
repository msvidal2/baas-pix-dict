/*
 *  baas-pix-dict 1.0 10/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.monitoring.config;

import com.picpay.banking.pix.core.domain.metrics.Metric;
import com.picpay.banking.pix.core.domain.metrics.MetricEvent;
import com.picpay.banking.reconciliation.repository.ReconciliationMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author rafael.braga
 * @version 1.0 10/02/2021
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ReconciliationMetricsConfig {

    private final ReconciliationMetricRepository reconciliationMetricRepository;

    @Bean
    public Metric reconciliationMetricEvents() {
        log.info("Construindo eventos para monitoramento de reconciliacao...");
        return Metric.builder()
            .domain("Reconciliation")
            .metricEvents(Collections.singletonList(MetricEvent.builder()
                                                        .description("reconciliations-executed_last_24_hours")
                                                        .value(reconciliationMetricRepository::countSyncExecutedLast24Hours)
                                                        .build()
                                                   ))
            .build();
    }

}

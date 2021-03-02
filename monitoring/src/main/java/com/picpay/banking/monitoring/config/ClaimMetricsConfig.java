/*
 *  baas-pix-dict 1.0 28/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.monitoring.config;

import com.picpay.banking.claim.repository.ClaimMetricRepository;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.metrics.Metric;
import com.picpay.banking.pix.core.domain.metrics.MetricEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author rafael.braga
 * @version 1.0 28/01/2021
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ClaimMetricsConfig {

    private static final LocalDateTime YESTERDAY = LocalDateTime.now().minusDays(1);
    private static final Integer DAYS_TO_OVERDUE = 24;

    private final ClaimMetricRepository claimMetricRepository;

    @Value("${picpay.ispb}")
    private Integer ispb;

    @Bean
    public Metric claimMetricEvents() {
        log.info("Construindo eventos de metricas para reinvidicacao");
        return Metric.builder()
            .domain("Claim")
            .metricEvents(Arrays.asList(
                MetricEvent.builder()
                    .description("open-claims-without-acknowledge")
                    .value(() -> claimMetricRepository.countByStatus(ClaimSituation.OPEN))
                    .build(),
                MetricEvent.builder()
                    .description("awaiting-possession-claims-donor")
                    .value(() -> claimMetricRepository.findAwaitingPossessionClaimsForDonor(ispb, YESTERDAY))
                    .build(),
                MetricEvent.builder()
                    .description("awaiting-portability-claims-donor")
                    .value(() -> claimMetricRepository.findAwaitingPortabilityClaimsForDonor(ispb, YESTERDAY))
                    .build(),
                MetricEvent.builder()
                    .description("awaiting-possession-claims-claimer")
                    .value(() -> claimMetricRepository.findAwaitingClaimToCancelForClaimer(ispb, DAYS_TO_OVERDUE))
                    .build()
                                       ))
            .build();
    }

}

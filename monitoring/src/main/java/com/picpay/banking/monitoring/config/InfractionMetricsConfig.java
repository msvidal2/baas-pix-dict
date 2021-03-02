/*
 *  baas-pix-dict 1.0 28/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.monitoring.config;

import com.picpay.banking.infraction.repository.InfractionMetricRepository;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.metrics.Metric;
import com.picpay.banking.pix.core.domain.metrics.MetricEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author rafael.braga
 * @version 1.0 28/01/2021
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class InfractionMetricsConfig {

    private final InfractionMetricRepository infractionMetricRepository;

    @Bean
    public Metric infractionMetricEvents() {
        log.trace("Construindo eventos de monitoramento de infraction...");
        return Metric.builder()
            .domain("Infraction")
            .metricEvents(
        Arrays.asList(MetricEvent.builder()
                                 .description("open-infractions_waiting_on_ack_by_third_party")
                                 .value(() -> infractionMetricRepository.countBySituation(InfractionReportSituation.OPEN))
                                 .build(),
                             MetricEvent.builder()
                                 .description("acknowledged-infractions_pending_analysis")
                                 .value(() -> infractionMetricRepository.countBySituation(InfractionReportSituation.ACKNOWLEDGED))
                                 .build(),
                             MetricEvent.builder()
                                 .description("analyzed-infractions_waiting_on_third_party")
                                 .value(() -> infractionMetricRepository.countBySituation(InfractionReportSituation.ANALYZED))
                                 .build())).build();
    }

}

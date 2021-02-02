/*
 *  baas-pix-dict 1.0 28/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.monitoring.infraction;

import com.picpay.banking.infraction.repository.InfractionMetricRepository;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.metrics.MetricEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 28/01/2021
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class InfractionMetricsConfig {

    private final InfractionMetricRepository infractionMetricRepository;

    @Bean
    public List<MetricEvent> metricEvents() {
        log.info("Construindo eventos de monitoramento de infraction...");
        return Arrays.asList(MetricEvent.builder()
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
                                 .build());
    }

}

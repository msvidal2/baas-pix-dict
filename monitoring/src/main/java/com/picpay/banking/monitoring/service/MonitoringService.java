/*
 *  baas-pix-dict 1.0 27/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.monitoring.service;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.domain.metrics.Metric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 27/01/2021
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringService {

    private final List<Metric> metrics;
    private static final String CATEGORY = "baas-pix-monitoring";
    private static final String EVENTS = "/events";

    @Trace(dispatcher = true, metricName = "baas-pix-dict-monitoring-service")
    @Scheduled(fixedDelayString = "${picpay.monitoring.delay}")
    public void sendEvents() {
        log.debug("Executando monitoramento baas-pix-dict...");
        NewRelic.setTransactionName(CATEGORY, EVENTS);
        metrics.forEach(domain -> domain.getMetricEvents().forEach(metric -> addCustomParameter(domain, metric)));
        log.debug("Execução de monitoramento finalizada.");
    }

    private void addCustomParameter(final Metric domain, final com.picpay.banking.pix.core.domain.metrics.MetricEvent metric) {
        log.debug("Enviando metrica {} do dominio {} para New Relic", metric.getDescription(), domain.getDomain());
        NewRelic.addCustomParameter(metric.getDescription(), metric.getValue().get());
    }

}

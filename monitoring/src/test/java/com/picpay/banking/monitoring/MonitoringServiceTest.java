/*
 *  baas-pix-dict 1.0 28/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.monitoring;

import com.picpay.banking.monitoring.service.MonitoringService;
import com.picpay.banking.pix.core.domain.metrics.Metric;
import com.picpay.banking.pix.core.domain.metrics.MetricEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class MonitoringServiceTest {

    private MonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        final var metricEvents = Metric.builder().metricEvents(List.of(MetricEvent.builder().description("test-metric").value(() -> 1L).build())).build();
        monitoringService = new MonitoringService(Collections.singletonList(metricEvents));
    }

    @Test
    void sendEvents() {
        assertThatCode(() -> monitoringService.sendEvents())
            .doesNotThrowAnyException();
    }

}
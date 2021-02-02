/*
 *  baas-pix-dict 1.0 28/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.monitoring;

import com.picpay.banking.pix.core.domain.metrics.MetricEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class MonitoringServiceTest {

    private MonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        final List<List<MetricEvent>> metricEvents = List.of(List.of(MetricEvent.builder().description("test-metric").value(() -> 1L).build()));
        monitoringService = new MonitoringService(metricEvents);
    }

    @Test
    void sendEvents() {
        assertThatCode(() -> monitoringService.sendEvents())
            .doesNotThrowAnyException();
    }

}
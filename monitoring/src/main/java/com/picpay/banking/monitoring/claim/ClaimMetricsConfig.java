/*
 *  baas-pix-dict 1.0 28/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.monitoring.claim;

import com.picpay.banking.claim.repository.ClaimMetricRepository;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.metrics.MetricEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 28/01/2021
 */
@Configuration
@RequiredArgsConstructor
public class ClaimMetricsConfig {

    private final ClaimMetricRepository claimMetricRepository;

    @Bean
    public List<MetricEvent> claimMetricEvents() {
        return Arrays.asList(MetricEvent.builder()
                                 .description("open-claims")
                                 .value(() -> claimMetricRepository.countByStatus(ClaimSituation.OPEN))
                                 .build(),
                             MetricEvent.builder()
                                 .description("awaiting-claims")
                                 .value(() -> claimMetricRepository.countByStatus(ClaimSituation.AWAITING_CLAIM))
                                 .build()
                            );
    }

}

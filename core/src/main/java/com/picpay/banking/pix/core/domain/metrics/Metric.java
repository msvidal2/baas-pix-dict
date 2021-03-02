/*
 *  baas-pix-dict 1.0 02/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.domain.metrics;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author rafael.braga
 * @version 1.0 02/03/2021
 */
@Getter
@Builder
public class Metric {

    private final List<MetricEvent> metricEvents;
    private final String domain;

}

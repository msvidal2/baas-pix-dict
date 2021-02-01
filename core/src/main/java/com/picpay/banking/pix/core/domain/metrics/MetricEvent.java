/*
 *  baas-pix-dict 1.0 27/01/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.core.domain.metrics;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Supplier;

/**
 * @author rafael.braga
 * @version 1.0 27/01/2021
 */
@Getter
@Builder
public class MetricEvent {

    private final String description;
    private final Supplier<Long> value;

}

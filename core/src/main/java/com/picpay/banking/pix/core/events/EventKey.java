/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.core.events;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Getter
@Builder
@EqualsAndHashCode
public class EventKey {

    @EqualsAndHashCode.Include
    private final EventType eventType;
    @EqualsAndHashCode.Include
    private final Domain domain;



}

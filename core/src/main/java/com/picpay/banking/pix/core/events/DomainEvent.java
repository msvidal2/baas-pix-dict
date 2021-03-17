/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.core.events;

import com.picpay.banking.pix.core.events.data.ErrorEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Getter
@Builder
@AllArgsConstructor
public class DomainEvent<T> {

    private final EventType eventType;
    private final Domain domain;
    private final T source;
    private final ErrorEvent errorEvent;
    private final String requestIdentifier;

}

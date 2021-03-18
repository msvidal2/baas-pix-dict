/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.processor;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Component(value = "createInfractionOnBacenProcessor")
public class InfractionReportCreateBacenProcessor implements EventProcessor {

    @Override
    public void process(final DomainEvent domainEvent) {

    }

}

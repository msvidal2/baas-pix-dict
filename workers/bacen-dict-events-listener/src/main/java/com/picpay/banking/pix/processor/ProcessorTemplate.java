/*
 *  baas-pix-dict 1.0 18/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.pix.processor;

import com.picpay.banking.exceptions.BacenException;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;

/**
 * @author rafael.braga
 * @version 1.0 18/03/2021
 */
public abstract class ProcessorTemplate<T> implements EventProcessor<T> {

    protected abstract DomainEvent<T> failedEvent(final DomainEvent<T> domainEvent, Exception e);

    protected DomainEvent<T> process(final DomainEvent<T> domainEvent) {
        try {
            return listen(domainEvent);
        } catch (BacenException e) {
            if (e.isRetryable)
                throw e;

            return failedEvent(domainEvent, e);
        }
    }

}

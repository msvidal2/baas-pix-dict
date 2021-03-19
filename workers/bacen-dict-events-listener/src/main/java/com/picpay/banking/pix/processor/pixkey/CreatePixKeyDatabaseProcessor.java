/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.processor.pixkey;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.CreateDatabasePixKeyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component(value = "createPixKeyDatabaseProcessor")
public class CreatePixKeyDatabaseProcessor implements EventProcessor<PixKeyEventData> {

    private final CreateDatabasePixKeyUseCase createDatabasePixKeyUseCase;

    @Override
    @Transactional
    public DomainEvent<PixKeyEventData> process(final DomainEvent<PixKeyEventData> domainEvent) {
        createDatabasePixKeyUseCase.execute(domainEvent.getSource());
        return null;
    }

}

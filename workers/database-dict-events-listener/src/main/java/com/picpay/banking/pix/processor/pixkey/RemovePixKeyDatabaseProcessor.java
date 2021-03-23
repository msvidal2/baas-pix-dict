/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.processor.pixkey;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.RemoveDatabasePixKeyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemovePixKeyDatabaseProcessor implements EventProcessor<PixKeyEventData> {

    private final RemoveDatabasePixKeyUseCase removeDatabasePixKeyUseCase;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DomainEvent<PixKeyEventData> process(final DomainEvent<PixKeyEventData> domainEvent) {
        removeDatabasePixKeyUseCase.execute(domainEvent.getSource());
        return null;
    }

}

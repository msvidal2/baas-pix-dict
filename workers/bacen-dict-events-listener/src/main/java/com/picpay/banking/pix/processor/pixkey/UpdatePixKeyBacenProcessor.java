/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.processor.pixkey;

import com.picpay.banking.exceptions.BacenException;
import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.ErrorEvent;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateBacenPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateDatabasePixKeyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component(value = "updatePixKeyBacenProcessor")
public class UpdatePixKeyBacenProcessor implements EventProcessor<PixKeyEventData> {

    private final UpdateBacenPixKeyUseCase updateBacenPixKeyUseCase;

    @Override
    public DomainEvent<PixKeyEventData> process(final DomainEvent<PixKeyEventData> domainEvent) {
        var pixkeyEventData = updateBacenPixKeyUseCase.execute(domainEvent.getRequestIdentifier(), domainEvent.getSource());
        return DomainEvent.<PixKeyEventData>builder()
                .eventType(EventType.PIX_KEY_UPDATED_BACEN)
                .domain(Domain.PIX_KEY)
                .source(PixKeyEventData.from(pixkeyEventData.toPixKey(), pixkeyEventData.getReason()))
                .requestIdentifier(pixkeyEventData.getRequestId().toString())
                .build();
    }

    public DomainEvent<PixKeyEventData> failedEvent(DomainEvent<PixKeyEventData> domainEvent, Exception e) {
        var error = (BacenException) e;
        var pixkeyEventData = domainEvent.getSource();
        return DomainEvent.<PixKeyEventData>builder()
                .eventType(EventType.PIX_KEY_FAILED_BACEN)
                .domain(Domain.PIX_KEY)
                .source(PixKeyEventData.from(pixkeyEventData.toPixKey(), pixkeyEventData.getReason()))
                .errorEvent(ErrorEvent.builder()
                        .code(error.getHttpStatus().name())
                        .description(error.getMessage())
                        .build())
                .requestIdentifier(pixkeyEventData.getRequestId().toString())
                .build();
    }

}

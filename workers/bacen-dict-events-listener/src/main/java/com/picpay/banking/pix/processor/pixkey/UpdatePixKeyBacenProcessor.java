/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.processor.pixkey;

import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.UpdateBacenPixKeyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_FAILED_BACEN;

@Slf4j
@RequiredArgsConstructor
@Component(value = "updatePixKeyBacenProcessor")
public class UpdatePixKeyBacenProcessor implements EventProcessor<PixKeyEventData> {

    private final UpdateBacenPixKeyUseCase updateBacenPixKeyUseCase;

    @Override
    public DomainEvent<PixKeyEventData> process(final DomainEvent<PixKeyEventData> domainEvent) {
        var pixkeyEventData = domainEvent.getSource();

        var pixKeyUpdated = updateBacenPixKeyUseCase.execute(
                domainEvent.getRequestIdentifier(),
                pixkeyEventData.toPixKey(),
                pixkeyEventData.getReason());

        return DomainEvent.<PixKeyEventData>builder()
                .eventType(EventType.PIX_KEY_UPDATED_BACEN)
                .domain(Domain.PIX_KEY)
                .source(PixKeyEventData.from(pixKeyUpdated, pixkeyEventData.getReason()))
                .requestIdentifier(pixkeyEventData.getRequestId().toString())
                .build();
    }

    @Override
    public EventType failedEventType() {
        return EventType.PIX_KEY_FAILED_BACEN;
    }

}

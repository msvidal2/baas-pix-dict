package com.picpay.banking.pix.processor.pixkey;

import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.RemoveBacenPixKeyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_FAILED_BACEN;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/03/21
 */
@Slf4j
@RequiredArgsConstructor
@Component(value = "removePixKeyBacenProcessor")
public class RemovePixKeyBacenProcessor implements EventProcessor<PixKeyEventData> {

    private final RemoveBacenPixKeyUseCase removeBacenPixKeyUseCase;

    @Override
    public DomainEvent<PixKeyEventData> process(final DomainEvent<PixKeyEventData> domainEvent) {
        var pixkeyEventData = domainEvent.getSource();

        var pixKeyRemoved = removeBacenPixKeyUseCase.execute(domainEvent.getRequestIdentifier(),
                                                             pixkeyEventData.toPixKey(), pixkeyEventData.getReason());

        return DomainEvent.<PixKeyEventData>builder()
            .eventType(EventType.PIX_KEY_REMOVED_BACEN)
            .domain(Domain.PIX_KEY)
            .source(PixKeyEventData.from(pixKeyRemoved, pixkeyEventData.getReason()))
            .requestIdentifier(pixKeyRemoved.getRequestId().toString())
            .build();
    }

    public EventType failedEventType() {
        return PIX_KEY_FAILED_BACEN;
    }

}

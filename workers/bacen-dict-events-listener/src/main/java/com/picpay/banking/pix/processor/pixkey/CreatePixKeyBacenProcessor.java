package com.picpay.banking.pix.processor.pixkey;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyBacenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.picpay.banking.pix.core.events.Domain.PIX_KEY;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_CREATED_BACEN;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_FAILED_BACEN;

@RequiredArgsConstructor
@Component(value = "createPixKeyBacenProcessor")
public class CreatePixKeyBacenProcessor implements EventProcessor<PixKeyEventData> {

    private final CreatePixKeyBacenUseCase createPixKeyBacenUseCase;

    @Override
    public DomainEvent<PixKeyEventData> process(DomainEvent<PixKeyEventData> domainEvent) {
        var eventData = domainEvent.getSource();

        var createdPixKey = createPixKeyBacenUseCase.execute(domainEvent.getRequestIdentifier(),
                eventData.toPixKey(),
                eventData.getReason());

        return DomainEvent.<PixKeyEventData>builder()
                .eventType(PIX_KEY_CREATED_BACEN)
                .domain(PIX_KEY)
                .source(PixKeyEventData.from(createdPixKey, eventData.getReason()))
                .requestIdentifier(domainEvent.getRequestIdentifier())
                .build();
    }

    @Override
    public EventType failedEventType() {
        return PIX_KEY_FAILED_BACEN;
    }

}

package com.picpay.banking.pix.processor.pixkey;

import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.CreateDatabasePixKeyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class CreatePixKeyDatabaseProcessor implements EventProcessor<PixKeyEventData> {

    private final CreateDatabasePixKeyUseCase createDatabasePixKeyUseCase;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DomainEvent<PixKeyEventData> process(final DomainEvent<PixKeyEventData> domainEvent) {
        createDatabasePixKeyUseCase.execute(domainEvent.getSource());
        return null;
    }

}

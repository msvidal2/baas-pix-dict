package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.usecase.reconciliation.services.ContentIdentifierEventValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
public class ContentIdentifierEventRecordUseCase {

    private final ContentIdentifierEventPort port;
    private final ContentIdentifierEventValidator validator;

    public void execute(ReconciliationEvent... events) {
        Stream.of(events).forEach(event -> {
            try {
                validator.validate(event);
                port.save(event);
            } catch (Exception e) {
                // TODO: Add treatment
            }
        });
    }

}

package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.domain.ReconciliationSyncEvent;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.validators.reconciliation.ReconciliationSyncEventValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
public class ContentIdentifierEventRecordUseCase {

    private final ContentIdentifierEventPort contentIdentifierEventPort;

//    public void execute(ReconciliationSyncEvent event) {
//        ReconciliationSyncEventValidator.validate(event);
//        List<ReconciliationEvent> reconciliationEvents = event.generateContentIdentifierEvents();
//        reconciliationEvents.forEach(contentIdentifierEventPort::save);
//    }

    public void execute(ReconciliationEvent... events) {
        Stream.of(events).forEach(contentIdentifierEventPort::save);
    }
}

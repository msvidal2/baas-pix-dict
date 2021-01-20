package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
public class ContentIdentifierEventRecordUseCase {

    private final ContentIdentifierEventPort contentIdentifierEventPort;

    public void execute(ReconciliationEvent... events) {
        Stream.of(events).forEach(contentIdentifierEventPort::save);
    }

}

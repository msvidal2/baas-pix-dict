package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pix.core.validators.reconciliation.ContentIdentifierEventValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ContentIdentifierEventRecordUseCase {

    private final ContentIdentifierEventPort port;

    public void execute(ReconciliationEvent event) {
        ContentIdentifierEventValidator.validate(event);
        port.save(event);
    }

}

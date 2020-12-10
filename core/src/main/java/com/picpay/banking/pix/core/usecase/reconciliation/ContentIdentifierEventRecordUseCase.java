package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.usecase.reconciliation.services.ContentIdentifierEventRecordService;
import com.picpay.banking.pix.core.usecase.reconciliation.services.ContentIdentifierEventValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ContentIdentifierEventRecordUseCase {

    private final ContentIdentifierEventRecordService service;
    private final ContentIdentifierEventValidator validator;

    public void execute(ReconciliationEvent event) throws Exception {
        validator.validate(event);

        switch (event.getAction()) {
            case ADD: service.add(event);
            case UPDATE: service.update(event);
            case REMOVE: service.remove(event);
        }
    }

}

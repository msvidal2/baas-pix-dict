package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.ports.reconciliation.ContentIdentifierEventPort;
import com.picpay.banking.pix.sync.repositories.ContentIdentifierEventRepository;
import lombok.AllArgsConstructor;

import static com.picpay.banking.pix.sync.entities.ContentIdentifierEventEntity.ContentIdentifierEventType;
import static com.picpay.banking.pix.sync.entities.ContentIdentifierEventEntity.fromDomain;

@AllArgsConstructor
public class ContentIdentifierEventPortImpl implements ContentIdentifierEventPort {

    private final ContentIdentifierEventRepository eventRepository;

    @Override
    public void save(ReconciliationEvent event) {
        eventRepository.save(fromDomain(event, ContentIdentifierEventType.ADD));
    }

    @Override
    public void update(ReconciliationEvent event) {
        eventRepository.save(fromDomain(event, ContentIdentifierEventType.REMOVE));
        eventRepository.save(fromDomain(event, ContentIdentifierEventType.ADD));
    }

    @Override
    public void remove(ReconciliationEvent event) {
        eventRepository.save(fromDomain(event, ContentIdentifierEventType.REMOVE));
    }

    @Override
    public ReconciliationEvent find(ReconciliationEvent event) { return null; }
}

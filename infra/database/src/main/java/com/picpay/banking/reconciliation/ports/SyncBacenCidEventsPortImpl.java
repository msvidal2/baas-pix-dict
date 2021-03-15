package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncBacenCidEvents;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncBacenCidEventsPort;
import com.picpay.banking.reconciliation.entity.BacenCidEventEntity;
import com.picpay.banking.reconciliation.entity.SyncBacenCidEventsEntity;
import com.picpay.banking.reconciliation.repository.BacenCidEventRepository;
import com.picpay.banking.reconciliation.repository.SyncBacenCidEventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SyncBacenCidEventsPortImpl implements SyncBacenCidEventsPort {

    private final BacenCidEventRepository bacenCidEventRepository;
    private final SyncBacenCidEventsRepository syncBacenCidEventsRepository;

    @Override
    public Optional<SyncBacenCidEvents> getSyncBacenCid(final KeyType keyType) {
        return syncBacenCidEventsRepository.findById(keyType)
            .map(SyncBacenCidEventsEntity::toDomain);
    }

    @Override
    public void saveSyncBacenCidEvents(final SyncBacenCidEvents syncBacenCidEvents) {
        syncBacenCidEventsRepository.save(SyncBacenCidEventsEntity.from(syncBacenCidEvents));
    }

    @Override
    public void saveAllBacenCidEvent(final KeyType keyType, final Set<BacenCidEvent> bacenCidEvents) {
        bacenCidEventRepository.saveAll(bacenCidEvents.stream()
            .map(bacenCidEvent -> BacenCidEventEntity.from(bacenCidEvent, keyType))
            .collect(Collectors.toList()));
    }

}

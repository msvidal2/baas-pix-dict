package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncBacenCidEvents;

import java.util.Optional;
import java.util.Set;

public interface SyncBacenCidEventsPort {

    Optional<SyncBacenCidEvents> getSyncBacenCid(KeyType keyType);

    void saveSyncBacenCidEvents(SyncBacenCidEvents syncBacenCidEvents);

    void saveAllBacenCidEvent(KeyType keyType, Set<BacenCidEvent> bacenCidEvents);

}

package com.picpay.banking.pix.core.domain.reconciliation;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class SyncBacenCidEvents {

    private KeyType keyType;
    private LocalDateTime lastSyncWithBacen;

    public void syncWithBacen(final Collection<BacenCidEvent> bacenCidEvents) {
        var bacenCidEventMostRecent = bacenCidEvents.stream().max(Comparator.comparing(BacenCidEvent::getEventOnBacenAt));
        bacenCidEventMostRecent.ifPresent(bacenCidEvent -> this.lastSyncWithBacen = bacenCidEvent.getEventOnBacenAt());
    }

}

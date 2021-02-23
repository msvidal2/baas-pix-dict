package com.picpay.banking.pix.core.usecase.reconciliation;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncBacenCidEvents;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifier;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncBacenCidEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class SincronizeCIDEventsUseCase {

    private final SyncBacenCidEventsPort syncBacenCidEventsPort;
    private final SyncVerifierPort syncVerifierPort;
    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;

    public void syncByKeyType(KeyType keyType) {
        log.info("SincronizeCIDEventsUseCase_started: {}", kv("keyType", keyType));
        var startCurrentTimeMillis = System.currentTimeMillis();

        SyncBacenCidEvents syncBacenCidEvents = syncBacenCidEventsPort.getSyncBacenCid(keyType)
            .orElseGet(() -> createSyncBacenCidAfterLastSyncVerifier(keyType));

        var bacenCidEvents = bacenContentIdentifierEventsPort.list(syncBacenCidEvents.getKeyType(), syncBacenCidEvents.getLastSyncWithBacen());

        syncBacenCidEvents.syncWithBacen(bacenCidEvents);

        syncBacenCidEventsPort.saveAllBacenCidEvent(syncBacenCidEvents.getKeyType(), bacenCidEvents);
        syncBacenCidEventsPort.saveSyncBacenCidEvents(syncBacenCidEvents);

        final int MILLISECONDS_TO_SECONDS = 1000;
        log.info("SincronizeCIDEventsUseCase_ended: {}, {}, {}",
            kv("keyType", keyType),
            kv("countBacenCidEvents", bacenCidEvents.size()),
            kv("totalRunTime_in_seconds", (System.currentTimeMillis() - startCurrentTimeMillis) / MILLISECONDS_TO_SECONDS));
    }

    private SyncBacenCidEvents createSyncBacenCidAfterLastSyncVerifier(KeyType keyType) {
        var syncVerifier = syncVerifierPort.getLastSuccessfulVsync(keyType)
            .orElseGet(() -> SyncVerifier.builder()
                .keyType(keyType)
                .synchronizedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .build());

        return SyncBacenCidEvents.builder()
            .keyType(keyType)
            .lastSyncWithBacen(syncVerifier.getSynchronizedAt())
            .build();
    }

}

package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncBacenCidEvents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sync_bacen_cid_events")
public class SyncBacenCidEventsEntity {

    @Id
    @Enumerated(EnumType.STRING)
    private KeyType keyType;

    @Column(name = "last_sync_with_bacen", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime lastSyncWithBacen;

    public static SyncBacenCidEventsEntity from(final SyncBacenCidEvents syncBacenCidEvents) {
        return SyncBacenCidEventsEntity.builder()
            .keyType(syncBacenCidEvents.getKeyType())
            .lastSyncWithBacen(syncBacenCidEvents.getLastSyncWithBacen())
            .build();
    }

    public SyncBacenCidEvents toDomain() {
        return SyncBacenCidEvents.builder()
            .keyType(keyType)
            .lastSyncWithBacen(lastSyncWithBacen)
            .build();
    }

}

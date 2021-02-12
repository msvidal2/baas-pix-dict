package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bacen_cid_events")
public class BacenCidEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "key_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private KeyType keyType;

    @Column(name = "cid", nullable = false)
    private String cid;

    @Column(name = "event_on_bacen_at", nullable = false)
    private LocalDateTime eventOnBacenAt;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReconciliationAction type;

    public static BacenCidEventEntity from(final BacenCidEvent bacenCidEvent, final KeyType keyType) {
        return BacenCidEventEntity.builder()
            .keyType(keyType)
            .cid(bacenCidEvent.getCid())
            .eventOnBacenAt(bacenCidEvent.getEventOnBacenAt())
            .type(bacenCidEvent.getAction())
            .build();
    }

    public BacenCidEvent toDomain() {
        return BacenCidEvent.builder()
            .cid(cid)
            .action(type)
            .eventOnBacenAt(eventOnBacenAt)
            .build();
    }

}

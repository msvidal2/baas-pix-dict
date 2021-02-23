package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationEvent;
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
import java.time.ZoneId;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "content_identifier_event")
public class ContentIdentifierEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pix_key", nullable = false)
    private String key;

    @Column(name = "key_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private KeyType keyType;

    @Column(name = "cid", nullable = false)
    private String cid;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "event_on_bacen_at", nullable = false)
    private LocalDateTime eventOnBacenAt;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReconciliationAction type;

    public ReconciliationEvent toDomain() {
        return ReconciliationEvent.builder()
                .cid(this.cid)
                .key(this.key)
                .keyType(this.keyType)
                .action(this.type)
                .eventOnBacenAt(this.eventOnBacenAt)
                .build();
    }

    public static ContentIdentifierEventEntity fromDomain(ReconciliationEvent event) {
        return ContentIdentifierEventEntity.builder()
                .cid(event.getCid())
                .key(event.getKey())
                .keyType(event.getKeyType())
                .createdAt(LocalDateTime.now(ZoneId.of("UTC")))
                .eventOnBacenAt(event.getEventOnBacenAt())
                .type(event.getAction())
                .build();
    }

}



package com.picpay.banking.pix.sync.entities;

import com.picpay.banking.pix.core.domain.ReconciliationAction;
import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import java.time.LocalDateTime;
import java.util.Arrays;

import static javax.persistence.EnumType.STRING;

@Data
@Builder
@Entity(name = "content-identifier-event")
public class ContentIdentifierEventEntity {

    @Id
    private String id;

    private String cid;

    private String key;

    @Enumerated(value = STRING)
    private ReconciliationKeyTypeEntity keyType;

    private LocalDateTime createAt;

    private LocalDateTime keyOwnershipDate;

    private ContentIdentifierEventType type;

    public ReconciliationEvent toDomain() {
        return ReconciliationEvent.builder()
                .cid(this.cid)
                .key(this.key)
                .keyType(this.keyType.getKeyType())
                // TODO: check if action type is needed
                .build();
    }

    public static ContentIdentifierEventEntity fromDomain(
            ReconciliationEvent event,
            ContentIdentifierEventType eventType
    ) {
        return ContentIdentifierEventEntity.builder()
                .cid(event.getCid())
                .key(event.getKey())
                .keyType(ReconciliationKeyTypeEntity.resolve(event.getKeyType()))
                .keyOwnershipDate(event.getKeyOwnershipDate())
                .type(eventType)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public enum ContentIdentifierEventType {
        ADD(ReconciliationAction.ADD),
        REMOVE(ReconciliationAction.REMOVE);

        private final ReconciliationAction reconciliationAction;
    }
}

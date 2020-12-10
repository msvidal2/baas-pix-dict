package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.ContentIdentifierEvent.ContentIdentifierEventType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

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

    @JoinColumns({
        @JoinColumn(name = "pix_key", insertable = false, updatable = false),
        @JoinColumn(name = "key_type", insertable = false, updatable = false)})
    @ManyToOne(fetch = FetchType.LAZY)
    private PixKeyEntity pixKey;

    @Column(name = "cid", nullable = false)
    private String cid;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "event_on_bacen_at", nullable = false)
    private LocalDateTime eventOnBacenAt;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentIdentifierEventType type;

    public static ContentIdentifierEvent toContentIdentifierEvent(final ContentIdentifierEventEntity contentIdentifierEventEntity) {
        return ContentIdentifierEvent.builder()
            .cid(contentIdentifierEventEntity.getCid())
            .contentIdentifierType(contentIdentifierEventEntity.getType())
            .eventOnBacenAt(contentIdentifierEventEntity.getEventOnBacenAt())
            .build();
    }

}



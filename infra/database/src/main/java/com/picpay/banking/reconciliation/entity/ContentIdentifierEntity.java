package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
@Entity(name = "content_identifier")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContentIdentifierEntity {

    @Id
    @Column
    private String cid;

    @Column
    @Enumerated(EnumType.STRING)
    private KeyType keyType;

    @Column
    private String key;

    @Column(name = "creation_date")
    @Builder.Default
    private LocalDateTime requestTime = LocalDateTime.now();

    @JoinColumn(name = "key", insertable = false, updatable = false)
    @JoinColumn(name = "keyType", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PixKeyEntity pixKey;

    public static ContentIdentifierEntity from(final ContentIdentifier contentIdentifier) {
        return ContentIdentifierEntity.builder()
            .cid(contentIdentifier.getCid())
            .keyType(contentIdentifier.getKeyType())
            .key(contentIdentifier.getKey())
            .build();
    }

    public ContentIdentifier toDomain() {
        return ContentIdentifier.builder()
            .cid(cid)
            .keyType(keyType)
            .key(key)
            .pixKey(pixKey == null ? null : pixKey.toPixKey())
            .build();
    }

}

package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
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

    public static ContentIdentifierEntity from(final ContentIdentifier contentIdentifier) {
        return ContentIdentifierEntity.builder()
            .cid(contentIdentifier.getCid())
            .keyType(contentIdentifier.getKeyType())
            .key(contentIdentifier.getKey())
            .build();
    }

}

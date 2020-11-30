package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pixkey.dto.request.KeyType;
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

import static com.picpay.banking.pix.core.domain.ContentIdentifierFile.StatusContentIdentifierFile;

/**
 * @author Luis Silva
 * @version 1.0 26/11/2020
 */
@Entity(name = "content_identifier_file")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContentIdentifierFileEntity {

    @Id
    @Column
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column
    private StatusContentIdentifierFile status;

    @Column
    @Enumerated(EnumType.STRING)
    private KeyType keyType;

    @Column(name = "request_date")
    private LocalDateTime requestTime;

    @Column
    private String url;

    @Column
    private Long length;

    @Column
    private String sha256;

    public static ContentIdentifierFileEntity from(final ContentIdentifierFile contentIdentifierFile) {
        return ContentIdentifierFileEntity.builder()
            .id(contentIdentifierFile.getId())
            .keyType(KeyType.resolve(contentIdentifierFile.getKeyType()))
            .length(contentIdentifierFile.getLength())
            .requestTime(contentIdentifierFile.getRequestTime())
            .sha256(contentIdentifierFile.getSha256())
            .status(contentIdentifierFile.getStatus())
            .url(contentIdentifierFile.getUrl())
        .build();
    }

    public  ContentIdentifierFile toDomain() {
        return ContentIdentifierFile.builder()
            .id(id)
            .keyType(keyType.getType())
            .length(length)
            .requestTime(requestTime)
            .sha256(sha256)
            .status(status)
            .url(url)
        .build();
    }

}

package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.SyncVerifier;
import com.picpay.banking.pix.core.domain.SyncVerifierResultType;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

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
@Entity(name = "sync_verifier")
public class SyncVerifierEntity {

    @Id
    @Enumerated(EnumType.STRING)
    private KeyTypeBacen keyType;

    @Column(nullable = false)
    private String vsync;

    @Column(name = "synchronized_at", nullable = false)
    private LocalDateTime synchronizedAt;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    public static SyncVerifierEntity from(final SyncVerifier syncVerifier) {
        return SyncVerifierEntity.builder()
            .keyType(KeyTypeBacen.resolve(syncVerifier.getKeyType()))
            .vsync(syncVerifier.getVsync())
            .synchronizedAt(syncVerifier.getSynchronizedAt())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public static SyncVerifier toSyncVerifier(final SyncVerifierEntity entity) {
        return SyncVerifier.builder()
            .keyType(entity.getKeyType().getType())
            .synchronizedAt(entity.getSynchronizedAt())
            .vsync(entity.getVsync())
            .syncVerifierResultType(SyncVerifierResultType.OK)
            .build();
    }

}
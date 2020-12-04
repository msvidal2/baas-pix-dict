package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.SyncVerifierResult;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
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
@Entity(name = "sync_verifier_historic")
public class SyncVerifierHistoricEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "key_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private KeyTypeBacen keyType;

    @Column(name = "vsync_start", nullable = false)
    private String vsyncStart;

    @Column(name = "vsync_end", nullable = false)
    private String vsyncEnd;

    @Column(name = "synchronized_start", nullable = false)
    private LocalDateTime synchronizedStart;

    @Column(name = "synchronized_end", nullable = false)
    private LocalDateTime synchronizedEnd;

    @Column(name = "result", nullable = false)
    @Enumerated(EnumType.STRING)
    private SyncVerifierResult result;

    public static SyncVerifierHistoricEntity from(final SyncVerifierHistoric syncVerifierHistoric) {
        return SyncVerifierHistoricEntity.builder()
            .id(syncVerifierHistoric.getId())
            .keyType(KeyTypeBacen.resolve(syncVerifierHistoric.getKeyType()))
            .vsyncStart(syncVerifierHistoric.getVsyncStart())
            .vsyncEnd(syncVerifierHistoric.getVsyncEnd())
            .synchronizedStart(syncVerifierHistoric.getSynchronizedStart())
            .synchronizedEnd(syncVerifierHistoric.getSynchronizedEnd())
            .result(syncVerifierHistoric.getSyncVerifierResult())
            .build();
    }

    public SyncVerifierHistoric toDomain() {
        return SyncVerifierHistoric.builder()
            .id(id)
            .keyType(keyType.getType())
            .vsyncStart(vsyncStart)
            .vsyncEnd(vsyncEnd)
            .synchronizedStart(synchronizedStart)
            .synchronizedEnd(synchronizedEnd)
            .syncVerifierResult(result)
            .build();
    }

}

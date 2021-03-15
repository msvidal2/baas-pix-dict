package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoric;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierResultType;
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
    private KeyType keyType;

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
    private SyncVerifierResultType result;

    @Enumerated(EnumType.STRING)
    @Column(name = "reconciliation_method")
    private SyncVerifierHistoric.ReconciliationMethod reconciliationMethod;

    public static SyncVerifierHistoricEntity from(final SyncVerifierHistoric syncVerifierHistoric) {
        return SyncVerifierHistoricEntity.builder()
            .id(syncVerifierHistoric.getId())
            .keyType(syncVerifierHistoric.getKeyType())
            .vsyncStart(syncVerifierHistoric.getVsyncStart())
            .vsyncEnd(syncVerifierHistoric.getVsyncEnd())
            .synchronizedStart(syncVerifierHistoric.getSynchronizedStart())
            .synchronizedEnd(syncVerifierHistoric.getSynchronizedEnd())
            .result(syncVerifierHistoric.getSyncVerifierResultType())
            .reconciliationMethod(syncVerifierHistoric.getReconciliationMethod())
            .build();
    }

    public SyncVerifierHistoric toDomain() {
        return SyncVerifierHistoric.builder()
            .id(id)
            .keyType(keyType)
            .vsyncStart(vsyncStart)
            .vsyncEnd(vsyncEnd)
            .synchronizedStart(synchronizedStart)
            .synchronizedEnd(synchronizedEnd)
            .syncVerifierResultType(result)
            .reconciliationMethod(reconciliationMethod)
            .build();
    }

}

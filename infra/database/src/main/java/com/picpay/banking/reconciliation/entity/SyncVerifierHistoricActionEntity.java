package com.picpay.banking.reconciliation.entity;

import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoricAction;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sync_verifier_historic_action")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class SyncVerifierHistoricActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_sync_verifier_historic", nullable = false)
    private Integer idSyncVerifierHistoric;

    @Column(name = "cid", nullable = false)
    private String cid;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private ReconciliationAction action;

    @JoinColumn(name = "id_sync_verifier_historic", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SyncVerifierHistoricEntity syncVerifierHistoric;

    @Type(type = "json")
    @Column(name = "content", columnDefinition = "json")
    private PixKeyEntity pixKey;

    @Column(name = "creation_date")
    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now(ZoneId.of("UTC"));

    public static SyncVerifierHistoricActionEntity from(final SyncVerifierHistoricAction syncVerifierHistoricAction) {
        return SyncVerifierHistoricActionEntity.builder()
            .idSyncVerifierHistoric(syncVerifierHistoricAction.getSyncVerifierHistoric().getId())
            .cid(syncVerifierHistoricAction.getCid())
            .action(syncVerifierHistoricAction.getAction())
            .pixKey(PixKeyEntity.from(syncVerifierHistoricAction.getPixKey(), Reason.RECONCILIATION))
            .build();
    }

}

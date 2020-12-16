package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.ReconciliationEvent;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface ContentIdentifierEventPort {

    void save(ReconciliationEvent event);

//    void update(ReconciliationEvent event);

//    void remove(ReconciliationEvent event);

    Set<ReconciliationEvent> findAllAfterLastSuccessfulVsync(KeyType keyType, LocalDateTime synchronizedStart); // TODO: trocar args para Reconciliation event

    Optional<PixKey> findPixKeyByContentIdentifier(String cid); // TODO: trocar args para Reconciliation event

}

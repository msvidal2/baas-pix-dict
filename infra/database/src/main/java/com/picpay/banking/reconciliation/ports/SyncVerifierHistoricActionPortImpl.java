package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.reconciliation.entity.SyncVerifierHistoricActionEntity;
import com.picpay.banking.reconciliation.repository.SyncVerifierHistoricActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SyncVerifierHistoricActionPortImpl implements SyncVerifierHistoricActionPort {

    private final SyncVerifierHistoricActionRepository syncVerifierHistoricActionRepository;

    @Override
    public void save(final SyncVerifierHistoricAction syncVerifierHistoricAction) {
        syncVerifierHistoricActionRepository.save(SyncVerifierHistoricActionEntity.from(syncVerifierHistoricAction));
    }

}

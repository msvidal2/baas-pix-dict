package com.picpay.banking.reconciliation.ports.picpay;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoricAction;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.reconciliation.entity.SyncVerifierHistoricActionEntity;
import com.picpay.banking.reconciliation.repository.SyncVerifierHistoricActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SyncVerifierHistoricActionPortImpl implements SyncVerifierHistoricActionPort {

    private final SyncVerifierHistoricActionRepository syncVerifierHistoricActionRepository;

    @Override
    public void saveAll(final Set<SyncVerifierHistoricAction> syncVerifierHistoricActions) {
        syncVerifierHistoricActionRepository.saveAll(syncVerifierHistoricActions.stream()
            .map(SyncVerifierHistoricActionEntity::from).collect(Collectors.toSet()));
    }

}

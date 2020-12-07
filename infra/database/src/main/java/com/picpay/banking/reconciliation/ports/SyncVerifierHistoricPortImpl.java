package com.picpay.banking.reconciliation.ports.picpay;

import com.picpay.banking.pix.core.domain.SyncVerifierHistoric;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.reconciliation.entity.SyncVerifierHistoricEntity;
import com.picpay.banking.reconciliation.repository.SyncVerifierHistoricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SyncVerifierHistoricPortImpl implements SyncVerifierHistoricPort {

    private final SyncVerifierHistoricRepository syncVerifierHistoricRepository;

    @Override
    public SyncVerifierHistoric save(final SyncVerifierHistoric syncVerifierHistoric) {
        return syncVerifierHistoricRepository.save(SyncVerifierHistoricEntity.from(syncVerifierHistoric)).toDomain();
    }

}

package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifier;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.reconciliation.entity.SyncVerifierEntity;
import com.picpay.banking.reconciliation.repository.SyncVerifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseVsyncPortImpl implements SyncVerifierPort {

    private final SyncVerifierRepository syncVerifierRepository;

    @Override
    public SyncVerifier getLastSuccessfulVsync(final KeyType keyType) {
        final int PIX_START_YEAR = 2020;
        final int PIX_START_MONTH = 1;

        return syncVerifierRepository.findById(keyType)
            .map(SyncVerifierEntity::toSyncVerifier)
            .orElseGet(() -> SyncVerifier.builder()
                .keyType(keyType)
                .synchronizedAt(LocalDateTime.of(PIX_START_YEAR, PIX_START_MONTH, 1, 0, 0))
                .build());
    }

    @Override
    public void save(final SyncVerifier syncVerifier) {
        syncVerifierRepository.save(SyncVerifierEntity.from(syncVerifier));
    }

}

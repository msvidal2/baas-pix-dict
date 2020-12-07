package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifier;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.reconciliation.entity.SyncVerifierEntity;
import com.picpay.banking.reconciliation.repository.SyncVerifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DatabaseVsyncPortImpl implements SyncVerifierPort {

    private final SyncVerifierRepository syncVerifierRepository;

    @Override
    public Optional<SyncVerifier> getLastSuccessfulVsync(final KeyType keyType) {
        return syncVerifierRepository.findById(KeyTypeBacen.resolve(keyType))
            .map(SyncVerifierEntity::toSyncVerifier);
    }

    @Override
    public void save(final SyncVerifier syncVerifier) {
        syncVerifierRepository.save(SyncVerifierEntity.from(syncVerifier));
    }

}

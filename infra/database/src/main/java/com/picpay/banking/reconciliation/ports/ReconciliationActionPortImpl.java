package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.events.PixKeyEvent;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.domain.reconciliation.SyncVerifierHistoricAction;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationActionPort;
import com.picpay.banking.pix.core.usecase.pixkey.PixKeyEventRegistryUseCase;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import com.picpay.banking.reconciliation.entity.SyncVerifierHistoricActionEntity;
import com.picpay.banking.reconciliation.repository.SyncVerifierHistoricActionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReconciliationActionPortImpl implements ReconciliationActionPort {

    private final PixKeyEventRegistryUseCase pixKeyEventRegistryUseCase;
    private final PixKeyRepository pixKeyRepository;
    private final SyncVerifierHistoricActionRepository syncVerifierHistoricActionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertPixKey(final SyncVerifierHistoricAction pixKeyAction) {
        final PixKeyEntity pixKeyEntity = PixKeyEntity.from(pixKeyAction.getPixKey(), Reason.RECONCILIATION);
        var pixKeyInserted = pixKeyRepository.save(pixKeyEntity).toPixKey();
        syncVerifierHistoricActionRepository.save(SyncVerifierHistoricActionEntity.from(pixKeyAction));
        // TODO: Confirmar se o requestIdentifier pode ser gerado dentro do processo de reconciliação
        pixKeyEventRegistryUseCase.execute(PixKeyEvent.CREATED, UUID.randomUUID().toString(), PixKeyEventData.from(pixKeyInserted), Reason.RECONCILIATION);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePixKey(final SyncVerifierHistoricAction oldPixKeyAction, final SyncVerifierHistoricAction newPixKeyAction) {
        final PixKeyEntity pixKeyEntity = PixKeyEntity.from(newPixKeyAction.getPixKey(), Reason.RECONCILIATION);
        var pixKeyUpdated = pixKeyRepository.save(pixKeyEntity).toPixKey();
        syncVerifierHistoricActionRepository.save(SyncVerifierHistoricActionEntity.from(oldPixKeyAction));
        syncVerifierHistoricActionRepository.save(SyncVerifierHistoricActionEntity.from(newPixKeyAction));
        // TODO: Confirmar se o requestIdentifier pode ser gerado dentro do processo de reconciliação
        pixKeyEventRegistryUseCase.execute(PixKeyEvent.UPDATED, UUID.randomUUID().toString(), PixKeyEventData.from(pixKeyUpdated), Reason.RECONCILIATION);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePixKey(final SyncVerifierHistoricAction pixKeyAction) {
        pixKeyRepository.findByCidAndReasonNot(pixKeyAction.getCid(), Reason.INACTIVITY)
            .ifPresent(pixKeyEntity -> {
                pixKeyEntity.setReason(Reason.INACTIVITY);
                var pixKeyInactivated = pixKeyRepository.save(pixKeyEntity).toPixKey();
                syncVerifierHistoricActionRepository.save(SyncVerifierHistoricActionEntity.from(pixKeyAction));
                // TODO: Confirmar se o requestIdentifier pode ser gerado dentro do processo de reconciliação
                pixKeyEventRegistryUseCase.execute(PixKeyEvent.REMOVED, UUID.randomUUID().toString(), PixKeyEventData.from(pixKeyInactivated), Reason.RECONCILIATION);
            });
    }

}

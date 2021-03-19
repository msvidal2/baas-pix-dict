package com.picpay.banking.reconciliation.ports;

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

import static com.picpay.banking.pix.core.domain.Reason.RECONCILIATION;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_CREATED_BACEN;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_REMOVED_BACEN;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_UPDATED_BACEN;

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
        final PixKeyEntity pixKeyEntity = PixKeyEntity.from(pixKeyAction.getPixKey(), RECONCILIATION);
        var pixKeyInserted = pixKeyRepository.save(pixKeyEntity).toPixKey();
        syncVerifierHistoricActionRepository.save(SyncVerifierHistoricActionEntity.from(pixKeyAction));
        pixKeyEventRegistryUseCase.execute(PIX_KEY_CREATED_BACEN, UUID.randomUUID().toString(), PixKeyEventData.from(pixKeyInserted, RECONCILIATION));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePixKey(final SyncVerifierHistoricAction oldPixKeyAction, final SyncVerifierHistoricAction newPixKeyAction) {
        final PixKeyEntity pixKeyEntity = PixKeyEntity.from(newPixKeyAction.getPixKey(), RECONCILIATION);
        var pixKeyUpdated = pixKeyRepository.save(pixKeyEntity).toPixKey();
        syncVerifierHistoricActionRepository.save(SyncVerifierHistoricActionEntity.from(oldPixKeyAction));
        syncVerifierHistoricActionRepository.save(SyncVerifierHistoricActionEntity.from(newPixKeyAction));
        pixKeyEventRegistryUseCase.execute(PIX_KEY_UPDATED_BACEN, UUID.randomUUID().toString(), PixKeyEventData.from(pixKeyUpdated, RECONCILIATION));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePixKey(final SyncVerifierHistoricAction pixKeyAction) {
        pixKeyRepository.findByCidAndReasonNot(pixKeyAction.getCid(), Reason.INACTIVITY)
            .ifPresent(pixKeyEntity -> {
                pixKeyEntity.setReason(Reason.INACTIVITY);
                var pixKeyInactivated = pixKeyRepository.save(pixKeyEntity).toPixKey();
                syncVerifierHistoricActionRepository.save(SyncVerifierHistoricActionEntity.from(pixKeyAction));
                pixKeyEventRegistryUseCase.execute(PIX_KEY_REMOVED_BACEN, UUID.randomUUID().toString(),
                    PixKeyEventData.from(pixKeyInactivated, RECONCILIATION));
            });
    }

}

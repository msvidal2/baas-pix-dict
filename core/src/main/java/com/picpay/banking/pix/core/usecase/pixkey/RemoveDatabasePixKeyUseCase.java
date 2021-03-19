package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import com.picpay.banking.pix.core.domain.reconciliation.ContentIdentifierEvent;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RemoveDatabasePixKeyUseCase {

    private final RemovePixKeyPort removePixKeyPort;
    private final ContentIdentifierEventPort contentIdentifierEventPort;

    public void execute(final PixKeyEventData pixKeyEventData) {
        final PixKey pixKey = pixKeyEventData.toPixKey();

        removePixKeyPort.remove(pixKey.getKey(), pixKey.getIspb())
            .ifPresent(this::createContentIdentifierEvent);
    }

    private void createContentIdentifierEvent(final PixKey pixKey) {
        final ContentIdentifierEvent event = ContentIdentifierEvent.builder()
            .keyType(pixKey.getType())
            .key(pixKey.getKey())
            .cid(pixKey.getCid())
            .eventOnBacenAt(pixKey.getUpdatedAt())
            .action(ReconciliationAction.REMOVED)
            .build();

        event.validate();

        contentIdentifierEventPort.save(event);
    }

}

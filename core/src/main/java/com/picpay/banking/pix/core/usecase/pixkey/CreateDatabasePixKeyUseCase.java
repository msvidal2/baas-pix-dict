package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.reconciliation.ReconciliationAction;
import com.picpay.banking.pix.core.domain.reconciliation.ContentIdentifierEvent;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateDatabasePixKeyUseCase {

    private final SavePixKeyPort savePixKeyPort;
    private final ContentIdentifierEventPort contentIdentifierEventPort;

    public void execute(final PixKeyEventData pixKeyEventData) {
        final PixKey pixKey = pixKeyEventData.toPixKey();
        pixKey.calculateCid();

        savePixKeyPort.savePixKey(pixKey, pixKeyEventData.getReason());

        final ContentIdentifierEvent event = ContentIdentifierEvent.builder()
            .keyType(pixKey.getType())
            .key(pixKey.getKey())
            .cid(pixKey.getCid())
            .eventOnBacenAt(pixKey.getUpdatedAt())
            .action(ReconciliationAction.ADDED)
            .build();

        event.validate();

        contentIdentifierEventPort.save(event);
    }

}

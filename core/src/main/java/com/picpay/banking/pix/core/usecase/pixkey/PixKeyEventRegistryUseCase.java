package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeyEvent;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PixKeyEventRegistryUseCase {

    private final PixKeyEventRegistryPort eventRegistryPort;

    public void execute(final PixKeyEvent event,
                        final String requestIdentifier,
                        final PixKey key,
                        final Reason reason) {

        eventRegistryPort.registry(event, requestIdentifier, key, reason);

    }

}

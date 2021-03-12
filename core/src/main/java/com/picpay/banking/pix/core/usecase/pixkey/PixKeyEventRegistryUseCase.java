package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeyEvent;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PixKeyEventRegistryUseCase {

    private final PixKeyEventRegistryPort eventRegistryPort;

    public void execute(@NonNull final PixKeyEvent event,
                        @NonNull final String requestIdentifier,
                        @NonNull final PixKey key,
                        @NonNull final Reason reason) {

        eventRegistryPort.registry(event, requestIdentifier, key, reason);
    }

}

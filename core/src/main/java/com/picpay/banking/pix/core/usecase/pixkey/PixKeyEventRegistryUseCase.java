package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PixKeyEventRegistryUseCase {

    private final PixKeyEventRegistryPort eventRegistryPort;

    public void execute(@NonNull final EventType event,
                        @NonNull final String requestIdentifier,
                        @NonNull final PixKeyEventData pixKeyEventData) {

        eventRegistryPort.registry(event, requestIdentifier, pixKeyEventData);
    }

}

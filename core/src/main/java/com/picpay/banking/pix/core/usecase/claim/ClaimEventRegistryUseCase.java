package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.ClaimEventData;
import com.picpay.banking.pix.core.ports.claim.ClaimEventRegistryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ClaimEventRegistryUseCase {

    private final ClaimEventRegistryPort eventRegistryPort;

    public void execute(final String requestIdentifier,
                        final EventType eventType,
                        final ClaimEventData claim) {

        eventRegistryPort.registry(requestIdentifier, eventType, claim);

    }

}

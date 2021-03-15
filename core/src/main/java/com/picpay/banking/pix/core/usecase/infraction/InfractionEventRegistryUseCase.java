package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.InfractionReportEvent;
import com.picpay.banking.pix.core.domain.infraction.events.InfractionReportEventData;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionEventRegistryPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InfractionEventRegistryUseCase {

    private final InfractionEventRegistryPort infractionEventRegistryPort;

    public void execute(@NonNull final InfractionReportEvent event,
                        @NonNull final String requestIdentifier,
                        @NonNull final InfractionReportEventData infractionReportEventData) {

        infractionEventRegistryPort.registry(event, requestIdentifier, infractionReportEventData);
    }

}

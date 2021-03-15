package com.picpay.banking.pix.core.ports.infraction.picpay;

import com.picpay.banking.pix.core.domain.InfractionReportEvent;
import com.picpay.banking.pix.core.domain.infraction.events.InfractionReportEventData;

public interface InfractionEventRegistryPort {

    void registry(InfractionReportEvent event, String requestIdentifier, InfractionReportEventData infractionReportEventData);

}

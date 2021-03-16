package com.picpay.banking.pix.core.ports.infraction.picpay;

import com.picpay.banking.pix.core.events.InfractionReportEvent;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;

public interface InfractionEventRegistryPort {

    void registry(InfractionReportEvent event, String requestIdentifier, InfractionReportEventData infractionReportEventData);

}

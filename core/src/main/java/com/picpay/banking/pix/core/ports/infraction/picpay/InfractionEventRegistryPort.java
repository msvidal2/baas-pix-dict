package com.picpay.banking.pix.core.ports.infraction.picpay;

import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;

public interface InfractionEventRegistryPort {

    void registry(EventType event, String requestIdentifier, InfractionReportEventData infractionReportEventData);

}

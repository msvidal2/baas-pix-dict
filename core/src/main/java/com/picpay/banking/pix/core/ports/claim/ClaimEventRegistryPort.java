package com.picpay.banking.pix.core.ports.claim;

import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.ClaimEventData;

public interface ClaimEventRegistryPort {

    void registry(String requestIdentifier, EventType eventType, ClaimEventData claim);

}

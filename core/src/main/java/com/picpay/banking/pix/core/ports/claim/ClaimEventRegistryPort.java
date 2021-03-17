package com.picpay.banking.pix.core.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.events.ClaimEventType;

public interface ClaimEventRegistryPort {

    void registry(String requestIdentifier, ClaimEventType eventType, Claim claim);

}

package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimEventType;

public interface ClaimEventRegistryPort {

    void persistClaimEvent(final Claim claim,
                           final String requestIdentifier,
                           final ClaimEventType claimEvent);
}
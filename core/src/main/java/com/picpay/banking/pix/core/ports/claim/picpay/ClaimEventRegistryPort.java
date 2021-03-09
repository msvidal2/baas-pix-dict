package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimEvent;

public interface ClaimEventRegistryPort {

    void persistClaimEvent(Claim claim,
                           String requestIdentifier,
                           ClaimEvent claimEvent);
}
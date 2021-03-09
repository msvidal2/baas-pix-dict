package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.domain.Claim;

public interface ClaimEventRegistryPort {

    void persistClaimEvent(Claim claim,
                           String requestIdentifier);
}
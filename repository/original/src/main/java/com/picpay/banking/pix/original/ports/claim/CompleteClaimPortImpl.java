package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.CompleteClaimPort;

public class CompleteClaimPortImpl implements CompleteClaimPort {
    @Override
    public Claim complete(Claim claim, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }
}

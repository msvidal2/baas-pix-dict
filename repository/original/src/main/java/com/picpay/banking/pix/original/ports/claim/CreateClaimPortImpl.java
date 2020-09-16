package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.CreateClaimPort;

public class CreateClaimPortImpl implements CreateClaimPort {
    @Override
    public Claim createPixKey(Claim claim, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }
}

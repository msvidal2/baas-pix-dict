package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.CreateClaimPort;

public class CreateClaimPortImpl implements CreateClaimPort {
    @Override
    public Claim createPixKey(Claim claim, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }
}

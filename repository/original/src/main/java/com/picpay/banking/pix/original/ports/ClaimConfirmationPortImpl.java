package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.ports.ClaimConfirmationPort;

public class ClaimConfirmationPortImpl implements ClaimConfirmationPort {
    @Override
    public Claim confirm(Claim claim, ClaimConfirmationReason reason, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }
}

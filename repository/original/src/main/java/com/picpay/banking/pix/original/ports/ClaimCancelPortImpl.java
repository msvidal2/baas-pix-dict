package com.picpay.banking.pix.original.ports;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.ports.ClaimCancelPort;

public class ClaimCancelPortImpl implements ClaimCancelPort {

    @Override
    public Claim cancel(Claim claim, boolean canceledClaimant, ClaimCancelReason reason, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }

}

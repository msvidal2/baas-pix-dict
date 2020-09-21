package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.ListClaimPort;

public class ListClaimPortImpl implements ListClaimPort {
    @Override
    public ClaimIterable list(Claim claim, Integer limit, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }
}

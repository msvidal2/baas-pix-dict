package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.ListPendingClaimPort;

public class ListPendingClaimPortImpl  implements ListPendingClaimPort {
    @Override
    public ClaimIterable list(Claim claim, Integer limit, String requestIdentifier) {
        throw new UnsupportedOperationException();
    }
}

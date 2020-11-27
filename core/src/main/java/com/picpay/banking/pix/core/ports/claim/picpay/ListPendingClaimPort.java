package com.picpay.banking.pix.core.ports.claim.picpay;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;

public interface ListPendingClaimPort {

    ClaimIterable list(final Claim claim, Integer limit, final String requestIdentifier);
}

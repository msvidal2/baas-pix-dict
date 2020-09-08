package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;

public interface ListPendingClaimPort {

    ClaimIterable list(final Claim claim, Integer limit, final String requestIdentifier);
}

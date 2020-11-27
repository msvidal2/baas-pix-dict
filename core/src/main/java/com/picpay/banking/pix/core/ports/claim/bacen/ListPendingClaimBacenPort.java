package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;

public interface ListPendingClaimBacenPort {

    ClaimIterable list(final Claim claim, Integer limit, final String requestIdentifier);
}

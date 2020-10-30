package com.picpay.banking.pix.core.ports.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;

public interface ListClaimPort {

    ClaimIterable list(final Claim claim, final Integer limit, final Boolean testeClaim, final Boolean isDonor, final String requestIdentifier);
}

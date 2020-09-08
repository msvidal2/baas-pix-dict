package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;

public interface ListClaimPort {

    ClaimIterable list(final Claim claim, final Integer limit, final String requestIdentifier);
}

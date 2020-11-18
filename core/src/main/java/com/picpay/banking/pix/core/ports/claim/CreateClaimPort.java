package com.picpay.banking.pix.core.ports.claim;


import com.picpay.banking.pix.core.domain.Claim;

public interface CreateClaimPort {

    Claim createClaim(final Claim claim, final String requestIdentifier);

}

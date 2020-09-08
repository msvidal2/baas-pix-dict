package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.Claim;

public interface FindClaimPort {

    Claim findClaim(final Claim claim);
}

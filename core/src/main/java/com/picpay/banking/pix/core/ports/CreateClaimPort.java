package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.Claim;

public interface CreateClaimPort {

    Claim createAddressingKey(final Claim claim, final String requestIdentifier);
}

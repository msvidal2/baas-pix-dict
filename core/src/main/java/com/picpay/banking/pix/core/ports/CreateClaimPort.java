package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.Claim;

public interface CreateClaimPort {

    Claim createPixKey(final Claim claim, final String requestIdentifier);
}

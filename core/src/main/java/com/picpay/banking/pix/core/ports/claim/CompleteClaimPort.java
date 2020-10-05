package com.picpay.banking.pix.core.ports.claim;


import com.picpay.banking.pix.core.domain.Claim;

public interface CompleteClaimPort {

    Claim complete(Claim claim, String requestIdentifier);

}
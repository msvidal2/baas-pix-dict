package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;

public interface CompleteClaimBacenPort {

    Claim complete(Claim claim, String requestIdentifier);

}
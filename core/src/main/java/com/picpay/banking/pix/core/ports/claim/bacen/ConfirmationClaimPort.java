package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;

public interface ConfirmationClaimPort {

    Claim confirm(Claim claim, ClaimConfirmationReason reason, String requestIdentifier);

}

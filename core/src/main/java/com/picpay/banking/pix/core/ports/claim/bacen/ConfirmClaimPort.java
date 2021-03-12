package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimReason;

public interface ConfirmClaimPort {

    Claim confirm(Claim claim, ClaimReason reason, String requestIdentifier);

}

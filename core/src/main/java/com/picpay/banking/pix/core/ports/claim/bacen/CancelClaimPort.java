package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;

public interface CancelClaimPort {

    Claim cancel(Claim claim, boolean canceledClaimant, ClaimCancelReason reason, String requestIdentifier);

}

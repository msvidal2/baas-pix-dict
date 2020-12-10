package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;

public interface CancelClaimBacenPort {

    Claim cancel(String claimId, ClaimCancelReason reason, int ispb, String requestIdentifier);

}

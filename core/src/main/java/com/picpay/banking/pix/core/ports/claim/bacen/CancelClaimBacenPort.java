package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimReason;
import com.picpay.banking.pix.core.domain.Reason;

public interface CancelClaimBacenPort {

    Claim cancel(String claimId, ClaimReason reason, int ispb, String requestIdentifier);

}

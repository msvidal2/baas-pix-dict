package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.Reason;

public interface CancelClaimBacenPort {

    Claim cancel(String claimId, Reason reason, int ispb, String requestIdentifier);

}

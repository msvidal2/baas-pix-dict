package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;

public interface CancelClaimPort {

    void cancel(Claim claim, ClaimCancelReason reason, String requestIdentifier);

}

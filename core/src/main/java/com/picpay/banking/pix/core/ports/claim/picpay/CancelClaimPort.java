package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.domain.Claim;

public interface CancelClaimPort {

    void cancel(Claim claim, String requestIdentifier);

}

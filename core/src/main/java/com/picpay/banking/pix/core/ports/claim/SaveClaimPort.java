package com.picpay.banking.pix.core.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;

public interface SaveClaimPort {

    Claim saveClaim(Claim claim, String requestIdentifier);

}

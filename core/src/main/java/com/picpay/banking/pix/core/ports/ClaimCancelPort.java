package com.picpay.banking.pix.core.ports;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;

public interface ClaimCancelPort {

    Claim cancel(Claim claim, boolean canceledClaimant, ClaimCancelReason reason, String requestIdentifier);

}

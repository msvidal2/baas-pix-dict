package com.picpay.banking.pix.core.ports.claim;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;

public interface ClaimConfirmationPort {

    Claim confirm(Claim claim, ClaimConfirmationReason reason, String requestIdentifier);

}

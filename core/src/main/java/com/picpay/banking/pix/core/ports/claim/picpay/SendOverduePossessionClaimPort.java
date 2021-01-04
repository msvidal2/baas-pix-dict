package com.picpay.banking.pix.core.ports.claim.picpay;

import com.picpay.banking.pix.core.domain.Claim;

public interface SendOverduePossessionClaimPort {

    void sendToConfirm(Claim claim);

    void sendToComplete(Claim claim);

}

package com.picpay.banking.pix.core.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;

public interface SendToProcessClaimNotificationPort {

    void send(Claim claim);

}

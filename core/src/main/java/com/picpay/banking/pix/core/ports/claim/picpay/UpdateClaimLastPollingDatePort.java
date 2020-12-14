package com.picpay.banking.pix.core.ports.claim.picpay;

import java.time.LocalDateTime;

public interface UpdateClaimLastPollingDatePort {

    void update(LocalDateTime endDate);

}

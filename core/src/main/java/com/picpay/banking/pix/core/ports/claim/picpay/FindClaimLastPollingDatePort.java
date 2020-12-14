package com.picpay.banking.pix.core.ports.claim.picpay;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FindClaimLastPollingDatePort {

    String LAST_POLLING_DATE_KEY = "CLAIM_LAST_POLLING_DATE";

    Optional<LocalDateTime> getDate();

}

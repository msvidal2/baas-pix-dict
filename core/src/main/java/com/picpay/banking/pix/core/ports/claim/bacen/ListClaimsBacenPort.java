package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;

import java.time.LocalDateTime;

public interface ListClaimsBacenPort {

    ClaimIterable list(Claim claim, Integer limit, Boolean isClaimer,
                       LocalDateTime startDate, LocalDateTime endDate);
}

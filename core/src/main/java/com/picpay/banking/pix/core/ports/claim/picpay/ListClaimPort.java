package com.picpay.banking.pix.core.ports.claim.picpay;


import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;

import java.time.LocalDateTime;

public interface ListClaimPort {

    ClaimIterable list(final Claim claim, final Integer limit, final Boolean testeClaim, final Boolean isDonor,
                       final LocalDateTime startDate, final LocalDateTime endDate, final String requestIdentifier);
}

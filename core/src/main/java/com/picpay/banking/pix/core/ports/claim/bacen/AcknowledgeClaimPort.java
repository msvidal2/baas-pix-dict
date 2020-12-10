package com.picpay.banking.pix.core.ports.claim.bacen;

import com.picpay.banking.pix.core.domain.Claim;

public interface AcknowledgeClaimPort {

    Claim acknowledge(final String claimId, final Integer ispb);

}

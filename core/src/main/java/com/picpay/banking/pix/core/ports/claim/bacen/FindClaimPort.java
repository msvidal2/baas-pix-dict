package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;

import java.util.Optional;

public interface FindClaimPort {

    Optional<Claim> findClaim(final String claimId, final Integer ispb, final boolean claimant);

}

package com.picpay.banking.pix.core.ports.claim.bacen;


import com.picpay.banking.pix.core.domain.Claim;

public interface FindClaimPort {

    Claim findClaim(final String claimId, final String ispb, final boolean reivindicador);


}

package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.FindClaimPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindClaimUseCase {

    private FindClaimPort findClaimPort;

    public Claim execute(final String claimId)  {
        return findClaimPort.findClaim(claimId);
    }

}
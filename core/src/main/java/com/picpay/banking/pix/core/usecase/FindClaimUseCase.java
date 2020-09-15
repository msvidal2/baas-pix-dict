package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.FindClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindClaimUseCase {

    private FindClaimPort findClaimPort;
    private DictItemValidator dictItemValidator;

    public Claim findClaimUseCase(final Claim claim)  {
        dictItemValidator.validate(claim);

        return findClaimPort.findClaim(claim);
    }

}
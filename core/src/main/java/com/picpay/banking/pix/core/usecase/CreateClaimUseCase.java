package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.CreateClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateClaimUseCase {

    private CreateClaimPort createClaimPort;
    private DictItemValidator validator;

    public Claim createClaim(final Claim claim, final String requestIdentifier) {

        validator.validate(claim);

        return createClaimPort.createAddressingKey(claim, requestIdentifier);
    }
}

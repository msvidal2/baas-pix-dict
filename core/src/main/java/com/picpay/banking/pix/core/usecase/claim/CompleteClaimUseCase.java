package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.claim.CompleteClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class CompleteClaimUseCase {

    private CompleteClaimPort completeClaimPort;

    private DictItemValidator<Claim> validator;

    public Claim execute(@NonNull final Claim claim,
                         @NonNull final String requestIdentifier) {

        validator.validate(claim);

        if (requestIdentifier.isBlank()) {
            throw new UseCaseException("You must inform a request identifier");
        }

        return completeClaimPort.complete(claim, requestIdentifier);
    }

}

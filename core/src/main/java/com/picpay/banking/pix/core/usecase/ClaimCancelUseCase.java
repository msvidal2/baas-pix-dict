package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.ports.ClaimCancelPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class ClaimCancelUseCase {

    private ClaimCancelPort claimCancelPort;

    private DictItemValidator<Claim> validator;

    public Claim execute(@NonNull final Claim claim,
                         final boolean canceledClaimant,
                         @NonNull final ClaimCancelReason reason,
                         @NonNull final String requestIdentifier) {

        validator.validate(claim);

        if(requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("The request identifier cannot be empty");
        }

        return claimCancelPort.cancel(claim, canceledClaimant, reason, requestIdentifier);
    }

}

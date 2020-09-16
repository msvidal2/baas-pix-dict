package com.picpay.banking.pix.core.usecase.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.claim.ClaimConfirmationPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class ClaimConfirmationUseCase {

    private ClaimConfirmationPort claimConfirmationPort;

    private DictItemValidator<Claim> validator;

    public Claim execute(Claim claim,
                         @NonNull final ClaimConfirmationReason reason,
                         @NonNull final String requestIdentifier) {

        validateRequestFields(reason, requestIdentifier);

        validator.validate(claim);

        return claimConfirmationPort.confirm(claim, reason, requestIdentifier);
    }

    public void validateRequestFields(final ClaimConfirmationReason reason, final String requestIdentifier) {
        if (Strings.isNullOrEmpty(requestIdentifier)) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }
        if (reason == null) {
            throw new IllegalArgumentException("reason can not be empty");
        }
    }

    private void validateParams(String claimId, int ispb, String requestIdentifier) {

        if(ispb <= 0) {
            throw new UseCaseException("Invalid ispb code");
        }

        if(requestIdentifier.isBlank()) {
            throw new UseCaseException("You must inform a request identifier");
        }
    }

}

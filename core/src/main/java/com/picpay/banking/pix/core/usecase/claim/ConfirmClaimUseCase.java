package com.picpay.banking.pix.core.usecase.claim;

import com.google.common.base.Strings;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class ConfirmClaimUseCase {

    private ConfirmClaimPort confirmClaimPort;

    private DictItemValidator<Claim> validator;

    public Claim execute(final Claim claim,
                         final ClaimConfirmationReason reason,
                         final String requestIdentifier) {

        validateRequestFields(reason, requestIdentifier);

        validator.validate(claim);

        Claim claimConfirmed = confirmClaimPort.confirm(claim, reason, requestIdentifier);

        if (claimConfirmed != null)
            log.info("Claim_confirmed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimConfirmed.getClaimId()));

        return claimConfirmed;
    }

    public void validateRequestFields(final ClaimConfirmationReason reason, final String requestIdentifier) {
        if (Strings.isNullOrEmpty(requestIdentifier)) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }
        if (reason == null) {
            throw new IllegalArgumentException("reason can not be empty");
        }
    }

}

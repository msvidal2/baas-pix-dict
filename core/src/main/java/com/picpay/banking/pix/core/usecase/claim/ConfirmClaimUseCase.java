package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.ports.claim.bacen.ConfirmClaimPort;
import com.picpay.banking.pix.core.validators.claim.ConfirmClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class ConfirmClaimUseCase {

    private ConfirmClaimPort confirmClaimPort;

    public Claim execute(final Claim claim,
                         final ClaimConfirmationReason reason,
                         final String requestIdentifier) {

        ConfirmClaimValidator.validate(claim, reason, requestIdentifier);

        Claim claimConfirmed = confirmClaimPort.confirm(claim, reason, requestIdentifier);

        if (claimConfirmed != null)
            log.info("Claim_confirmed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimConfirmed.getClaimId()));

        return claimConfirmed;
    }

}

package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CompleteClaimUseCase {

    private CompleteClaimPort completeClaimPort;

    private DictItemValidator<Claim> validator;

    public Claim execute(@NonNull final Claim claim,
                         @NonNull final String requestIdentifier) {

        validator.validate(claim);

        if (requestIdentifier.isBlank()) {
            throw new UseCaseException("You must inform a request identifier");
        }

        Claim claimCompleted = completeClaimPort.complete(claim, requestIdentifier);

        if (claimCompleted != null)
            log.info("Claim_completed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimCompleted.getClaimId()));

        return claimCompleted;
    }

}

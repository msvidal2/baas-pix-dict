package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CompleteClaimUseCase {

    private CompleteClaimBacenPort completeClaimBacenPort;
    private CompleteClaimPort completeClaimPort;
    private FindClaimPort findClaimPort;

    private DictItemValidator<Claim> validator;

    public Claim execute(@NonNull final Claim claim,
                         @NonNull final String requestIdentifier) {

        // TODO - corrigir padrao de validacao
        validator.validate(claim);

        if (requestIdentifier.isBlank()) {
            throw new UseCaseException("You must inform a request identifier");
        }

        validateClaimSituation(claim);
        Claim claimCompleted = completeClaimBacenPort.complete(claim, requestIdentifier);

        completeClaimPort.complete(claim, requestIdentifier);

        // TODO - criar chave

        if (claimCompleted != null)
            log.info("Claim_completed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimCompleted.getClaimId()));

        return claimCompleted;
    }

    private void validateClaimSituation(Claim claim){
        Optional<Claim> optClaim = findClaimPort.findClaim(claim.getClaimId(), claim.getIspb(), true);
        optClaim.ifPresent(claimResult -> {
            if(!ClaimSituation.CONFIRMED.equals(claimResult.getClaimSituation()))
                throw new ClaimException(ClaimError.JDPIRVN011);

            if(ClaimType.POSSESSION_CLAIM.equals(claimResult.getClaimType()) && claimResult.getCompletionThresholdDate().isAfter(LocalDateTime.now()))
                throw new ClaimException(ClaimError.JDPIRVN011);
        });
    }
}

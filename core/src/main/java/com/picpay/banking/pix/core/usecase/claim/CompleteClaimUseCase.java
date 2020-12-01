package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.claim.CompleteClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CompleteClaimUseCase {

    private CompleteClaimBacenPort completeClaimBacenPort;
    private CompleteClaimPort completeClaimPort;
    private FindClaimPort findClaimPort;

    private DictItemValidator<Claim> validator;

    public Claim execute(final Claim claim, final String requestIdentifier) {

        CompleteClaimValidator.validate(requestIdentifier, claim);
        CompleteClaimValidator.validateClaimSituation(findClaimPort.findClaim(claim.getClaimId(), claim.getIspb(), true));

        Claim claimCompleted = completeClaimBacenPort.complete(claim, requestIdentifier);
        completeClaimPort.complete(claim, requestIdentifier);

        // TODO - criar chave

        if (claimCompleted != null)
            log.info("Claim_completed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimCompleted.getClaimId()));

        return claimCompleted;
    }
}

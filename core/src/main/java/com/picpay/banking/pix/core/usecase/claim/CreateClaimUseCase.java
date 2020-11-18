package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CreateClaimUseCase {

    private final com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimPort createClaimPort;
    private final CreateClaimPort saveClaimPort;

    private DictItemValidator validator;

    public Claim execute(final Claim claim, final String requestIdentifier) {

        validator.validate(claim);

        Claim claimCreated = createClaimPort.createClaim(claim, requestIdentifier);
        Claim claimSaved = saveClaimPort.saveClaim(claim, requestIdentifier);

        if (claimSaved != null)
            log.info("Claim_created",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimCreated.getClaimId()));

        return claimSaved;
    }
}

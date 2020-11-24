package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CreateClaimUseCase {

    private final CreateClaimBacenPort createClaimPort;
    private final CreateClaimPort saveClaimPort;

    private DictItemValidator validator;

    public Claim execute(final Claim claim, final String requestIdentifier) {
        log.debug("CreateClaimUseCase - method execute");

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier cannot be empty");
        }

        validator.validate(claim);

        // Claim claimCreated = createClaimPort.createClaim(claim, requestIdentifier);

        /*if (claimCreated != null)
            log.info("Claim_created",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimCreated.getClaimId()));*/

        claim.setClaimId(UUID.randomUUID().toString());
        claim.setClaimSituation(ClaimSituation.OPEN);
        log.debug("CreateClaimUseCase - saving in db");
        Claim claimSaved = saveClaimPort.saveClaim(claim, requestIdentifier);

        log.debug("Success - " + claimSaved.toString());
        return claimSaved;
    }
}

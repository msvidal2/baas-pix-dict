package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.validators.claim.CreateClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CreateClaimUseCase {

    private final CreateClaimBacenPort createClaimPort;
    private final CreateClaimPort saveClaimPort;
    private final FindOpenClaimByKeyPort findOpenClaimByKeyPort;
    private final FindPixKeyPort findPixKeyPort;

    public Claim execute(final Claim claim, final String requestIdentifier) {
        CreateClaimValidator.validate(requestIdentifier, claim);

        validateClaimAlreadyExistsForKey(claim.getKey());
        validateClaimTypeInconsistent(claim);

        Claim claimCreated = createClaimPort.createClaim(claim, requestIdentifier);

        log.info("Claim_created",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimCreated.getClaimId()));

        return saveClaimPort.saveClaim(claimCreated, requestIdentifier);
    }

    private void validateClaimAlreadyExistsForKey(String key) {
        findOpenClaimByKeyPort.find(key).ifPresent(claim -> {
            throw new ClaimException(ClaimError.OPEN_CLAIM_ALREADY_EXISTS_FOR_KEY);
        });
    }

    private void validateClaimTypeInconsistent(Claim claim) {
        findPixKeyPort.findPixKey(claim.getKey()).ifPresent(pixKey -> {
            if (ClaimType.POSSESSION_CLAIM.equals(claim.getClaimType())
                    && pixKey.getTaxIdWithLeftZeros().equalsIgnoreCase(claim.getTaxIdWithLeftZeros())) {
                throw new ClaimException(ClaimError.KEY_ALREADY_BELONGS_TO_CUSTOMER);
            }
            if (ClaimType.PORTABILITY.equals(claim.getClaimType())
                    && !pixKey.getTaxIdWithLeftZeros().equalsIgnoreCase(claim.getTaxIdWithLeftZeros())) {
                throw new ClaimException(ClaimError.INCONSISTENT_PORTABILITY);
            }
        });
    }
}

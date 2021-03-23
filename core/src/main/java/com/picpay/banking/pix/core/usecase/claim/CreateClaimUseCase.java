package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.exception.ClaimError;
import com.picpay.banking.pix.core.exception.ClaimException;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CreateClaimUseCase {

    private final CreateClaimBacenPort createClaimPort;
    private final FindOpenClaimByKeyPort findOpenClaimByKeyPort;
    private final FindPixKeyPort findPixKeyPort;

    public Claim execute(final Claim claim, final String requestIdentifier) {
        validateClaimAlreadyExistsForKey(claim.getPixKey().getKey());

        var optionalPixKey = findPixKeyPort.findPixKey(claim.getPixKey().getKey());
        validateClaimTypeInconsistent(claim, optionalPixKey);

        var claimCreated = createClaimPort.createClaim(claim, requestIdentifier);
        optionalPixKey.ifPresent(claimCreated::setPixKey);

        log.info("Claim_created",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimCreated.getClaimId()));

        return claimCreated;
    }

    private void validateClaimAlreadyExistsForKey(String key) {
        findOpenClaimByKeyPort.find(key).ifPresent(claim -> {
            throw new ClaimException(ClaimError.OPEN_CLAIM_ALREADY_EXISTS_FOR_KEY);
        });
    }

    private void validateClaimTypeInconsistent(Claim claim, Optional<PixKey> optPixKey) {
        optPixKey.ifPresent(pixKey -> {
            if (!Objects.isNull(pixKey.getTaxIdWithLeftZeros())) {
                if (ClaimType.POSSESSION_CLAIM.equals(claim.getClaimType())
                        && pixKey.getTaxIdWithLeftZeros().equalsIgnoreCase(claim.getTaxIdWithLeftZeros())) {
                    throw new ClaimException(ClaimError.KEY_ALREADY_BELONGS_TO_CUSTOMER);
                }
                if (ClaimType.PORTABILITY.equals(claim.getClaimType())
                        && !pixKey.getTaxIdWithLeftZeros().equalsIgnoreCase(claim.getTaxIdWithLeftZeros())) {
                    throw new ClaimException(ClaimError.INCONSISTENT_PORTABILITY);
                }
            }
        });
    }

}

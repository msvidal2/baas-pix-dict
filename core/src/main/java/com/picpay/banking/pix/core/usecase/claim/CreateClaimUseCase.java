package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CreateClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CreateClaimUseCase {

    private final CreateClaimBacenPort createClaimPort;
    private final CreateClaimPort saveClaimPort;
    private final FindOpenClaimByKeyPort findClaimByKeyPort;
    private final FindPixKeyPort findPixKeyPort;

    private final DictItemValidator validator;

    public Claim execute(final Claim claim, final String requestIdentifier) {
        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier cannot be empty");
        }

        validator.validate(claim);

        validateClaimAlreadyExistsForKey(claim.getKey());

        validateClaimTypeInconsistent(claim);

        Claim claimCreated = createClaimPort.createClaim(claim, requestIdentifier);

        log.info("Claim_created",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimCreated.getClaimId()));

        return saveClaimPort.saveClaim(claimCreated, requestIdentifier);
    }

    private void validateClaimAlreadyExistsForKey(String key) {
        findClaimByKeyPort.find(key).ifPresent(claim -> {
            // lançar ClaimAlreadyExistsForKey pq existe uma reivindicação aberta para essa chave já
        });
    }

    private void validateClaimTypeInconsistent(Claim claim) {
        findPixKeyPort.findPixKey(claim.getKey()).ifPresent(pixKey -> {
            if (ClaimType.POSSESSION_CLAIM.equals(claim.getClaimType())) {
                if (pixKey.getTaxId().equalsIgnoreCase(claim.getCpfCnpj())) {
                    // lançar ClaimTypeInconsistent pq é uma posse e já tá com ele
                }
            }
            if (ClaimType.PORTABILITY.equals(claim.getClaimType())) {
                if (pixKey.getTaxId().equalsIgnoreCase(claim.getCpfCnpj())) {
                    // lançar ClaimTypeInconsistent pq é uma portabilidade sendo que a chave tá com outra pessoa
                }
            }
        });
    }

}

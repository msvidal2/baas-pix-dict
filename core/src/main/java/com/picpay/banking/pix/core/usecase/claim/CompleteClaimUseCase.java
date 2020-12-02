package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.validators.claim.CompleteClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CompleteClaimUseCase {

    private final CompleteClaimBacenPort completeClaimBacenPort;
    private final CompleteClaimPort completeClaimPort;

    private final FindClaimPort findClaimPort;

    private final CreatePixKeyBacenPort createPixKeyBacenPort;
    private final CreatePixKeyPort createPixKeyPort;

    public Claim execute(final Claim claim, final String requestIdentifier) {

        CompleteClaimValidator.validate(requestIdentifier, claim);
        CompleteClaimValidator.validateClaimSituation(findClaimPort.findClaim(claim.getClaimId(), claim.getIspb(), true));

        var claimCompleted = executeClaim(claim, requestIdentifier);
        var createdPixKey = createPixKeyForClaimer(claim, requestIdentifier);

        if (claimCompleted != null)
            log.info("Claim_completed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimCompleted.getClaimId()));

        if (createdPixKey != null)
            log.info("PixKey_created"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("key", createdPixKey.getKey()));

        return claimCompleted;
    }

    private Claim executeClaim(final Claim claim, final String requestIdentifier){
        Claim claimCompleted = completeClaimBacenPort.complete(claim, requestIdentifier);
        completeClaimPort.complete(claim, requestIdentifier);

        return claimCompleted;
    }

    private PixKey createPixKeyForClaimer(final Claim claim, final String requestIdentifier){
        PixKey pixKey = PixKey.builder()
                .type(claim.getKeyType())
                .key(claim.getKey())
                .accountOpeningDate(claim.getAccountOpeningDate())
                .accountType(claim.getAccountType())
                .accountNumber(claim.getAccountNumber())
                .branchNumber(claim.getBranchNumber())
                .taxId(claim.getCpfCnpj())
                .ispb(claim.getIspb())
                .personType(claim.getPersonType())
                .name(claim.getName())
                .fantasyName(claim.getFantasyName())
                .build();

        var createdPixKey = createPixKeyBacenPort.create(requestIdentifier, pixKey, CreateReason.CLIENT_REQUEST);
        createPixKeyPort.createPixKey(createdPixKey, CreateReason.CLIENT_REQUEST);

        return createdPixKey;
    }
}

package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.validators.claim.CompleteClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CompleteClaimUseCase {

    private final CompleteClaimBacenPort completeClaimBacenPort;
    private final CompleteClaimPort completeClaimPort;
    private final FindClaimPort findClaimPort;
    private final SavePixKeyPort createPixKeyPort;

    public Claim execute(final Claim claim, final String requestIdentifier) {

        CompleteClaimValidator.validate(requestIdentifier, claim);
        CompleteClaimValidator.validateClaimSituation(findClaimPort.findClaim(claim.getClaimId(), claim.getIspb(), true));

        var claimCompleted = executeClaim(claim, requestIdentifier);
        createPixKeyForClaimer(claimCompleted, randomUUID());

        return claimCompleted;
    }

    private Claim executeClaim(final Claim claim, final String requestIdentifier){
        Claim claimCompleted = completeClaimBacenPort.complete(claim, requestIdentifier);
        completeClaimPort.complete(claim, requestIdentifier);

        if (claimCompleted != null)
            log.info("Claim_completed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claimCompleted.getClaimId()));

        return claimCompleted;
    }

    private PixKey createPixKeyForClaimer(final Claim claim, final UUID requestIdentifier){
        PixKey pixKey = PixKey.builder()
                .type(claim.getKeyType())
                .key(claim.getKey())
                .ispb(claim.getIspb())
                .branchNumber(claim.getBranchNumber())
                .accountType(claim.getAccountType())
                .accountNumber(claim.getAccountNumber())
                .accountOpeningDate(claim.getAccountOpeningDate())
                .personType(claim.getPersonType())
                .taxId(claim.getCpfCnpj())
                .name(claim.getName())
                .fantasyName(claim.getFantasyName())
                .correlationId(claim.getCorrelationId())
                .createdAt(LocalDateTime.now())
                .accountOpeningDate(claim.getAccountOpeningDate())
                .startPossessionAt(LocalDateTime.now())
                .requestId(requestIdentifier)
                .build();

        pixKey.calculateCid();
        return createPixKeyPort.savePixKey(pixKey, Reason.CLIENT_REQUEST);
    }
}

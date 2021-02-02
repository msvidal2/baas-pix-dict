package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.bacen.FindClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.validators.claim.CompleteClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CompleteClaimUseCase {

    private final CompleteClaimBacenPort completeClaimBacenPort;
    private final CompleteClaimPort completeClaimPort;
    private final FindClaimPort findClaimPort;
    private final SavePixKeyPort createPixKeyPort;
    private final PixKeyEventPort pixKeyEventPort;

    public Claim execute(final Claim claim, final String requestIdentifier) {

        CompleteClaimValidator.validate(requestIdentifier, claim);
        CompleteClaimValidator.validateClaimSituation(findClaimPort.findClaim(claim.getClaimId(), claim.getIspb(), true));

        Claim claimCompleted = executeClaim(claim, requestIdentifier);
        createPixKeyForClaimer(claimCompleted, requestIdentifier);

        return claimCompleted;
    }

    private Claim executeClaim(final Claim claim, final String requestIdentifier) {
        Claim claimCompleted = completeClaimBacenPort.complete(claim, requestIdentifier);
        completeClaimPort.complete(claim, requestIdentifier);

        if (claimCompleted != null)
            log.info("Claim_completed",
                kv("requestIdentifier", requestIdentifier),
                kv("claimId", claimCompleted.getClaimId()));

        return claimCompleted;
    }

    private void createPixKeyForClaimer(final Claim claim, final String requestIdentifier) {
        PixKey pixKey = PixKey.builder()
            .type(claim.getPixKey().getType())
            .key(claim.getPixKey().getKey())
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
            .createdAt(LocalDateTime.now(ZoneId.of("UTC")))
            .accountOpeningDate(claim.getAccountOpeningDate())
            .startPossessionAt(LocalDateTime.now(ZoneId.of("UTC")))
            .requestId(UUID.fromString(requestIdentifier))
            .updatedAt(claim.getLastModifiedDate())
            .build();

        pixKey.calculateCid();

        save(pixKey);
        sendEvent(pixKey);
    }

    private void save(PixKey createdPixKey) {
        try {
            createPixKeyPort.savePixKey(createdPixKey, Reason.CLIENT_REQUEST);
        } catch (Exception e) {
            log.error("Claim_completed_saveError",
                kv("requestIdentifier", createdPixKey.getRequestId()),
                kv("key", createdPixKey.getKey()),
                kv("exception", e));
        }
    }

    private void sendEvent(PixKey createdPixKey) {
        try {
            pixKeyEventPort.pixKeyWasCreated(createdPixKey);
        } catch (Exception e) {
            log.error("Claim_completed_eventError",
                kv("requestIdentifier", createdPixKey.getRequestId()),
                kv("key", createdPixKey.getKey()),
                kv("exception", e));
        }
    }

}

package com.picpay.banking.claim.ports.picpay;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class CancelClaimPortImpl implements CancelClaimPort  {

    private ClaimRepository repository;

    @Override
    public void cancel(Claim claim, ClaimCancelReason reason, String requestIdentifier) {
        try {
            repository.save(ClaimEntity.from(claim));

            log.info("Claim_updated",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claim.getClaimId()));
        } catch (Exception e) {
            log.error("Claim_updatedError",
                    kv("requestIdentifier", requestIdentifier),
                    kv("claimId", claim.getClaimId()),
                    kv("exceptionMessage", e.getMessage()),
                    kv("exception", e));
        }
    }

}

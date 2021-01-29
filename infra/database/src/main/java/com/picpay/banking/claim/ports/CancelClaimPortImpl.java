package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.repository.ClaimRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CancelClaimPortImpl implements CancelClaimPort  {

    private final ClaimRepository repository;

    @Override
    public void cancel(Claim claim, String requestIdentifier){
            repository.save(ClaimEntity.from(claim));

            log.info("Claim_updated",
                    StructuredArguments.kv("requestIdentifier", requestIdentifier),
                    StructuredArguments.kv("claimId", claim.getClaimId()));
    }

}

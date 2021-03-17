package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.entity.ClaimEventEntity;
import com.picpay.banking.claim.repository.ClaimEventRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.events.ClaimEventType;
import com.picpay.banking.pix.core.ports.claim.ClaimEventRegistryPort;
import com.picpay.banking.pix.core.validators.idempotency.annotation.IdempotencyKey;
import com.picpay.banking.pix.core.validators.idempotency.annotation.ValidateIdempotency;
import com.picpay.banking.pix.core.ports.claim.picpay.ClaimCacheSavePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClaimEventRegistryPortImpl implements ClaimEventRegistryPort {

    private final ClaimEventRepository claimEventRepository;

    private final ClaimCacheSavePort claimCacheSavePort;

    @Override
    @ValidateIdempotency(Claim.class)
    public void registry(@IdempotencyKey String requestIdentifier, ClaimEventType eventType, Claim claim) {

        var claimEvent = ClaimEventEntity.of(
                requestIdentifier,
                claim,
                eventType);

        claimEventRepository.save(claimEvent);

        claimCacheSavePort.save(claim, requestIdentifier);
    }

}
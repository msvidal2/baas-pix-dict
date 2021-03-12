package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.entity.ClaimEvent;
import com.picpay.banking.claim.entity.ClaimEventTypeEntity;
import com.picpay.banking.claim.repository.ClaimEventRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimEventType;
import com.picpay.banking.pix.core.ports.claim.picpay.ClaimEventRegistryPort;
import com.picpay.banking.pix.core.validators.idempotency.annotation.IdempotencyKey;
import com.picpay.banking.pix.core.validators.idempotency.annotation.ValidateIdempotency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClaimEventRegistryPortImpl implements ClaimEventRegistryPort {

    private final ClaimEventRepository claimEventRepository;

    @Override
    @ValidateIdempotency(Claim.class)
    public void persistClaimEvent(final Claim claim,
                                  @IdempotencyKey final String requestIdentifier,
                                  final ClaimEventType claimEvent) {
        ClaimEntity entity = ClaimEntity.from(claim);
        ClaimEvent event = ClaimEvent.builder()
                .claim(entity)
                .data(entity)
                .type(ClaimEventTypeEntity.resolve(claimEvent))
                .build();

        claimEventRepository.save(event);
    }
}
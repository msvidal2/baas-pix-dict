package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.entity.ClaimEvent;
import com.picpay.banking.claim.entity.ClaimEventType;
import com.picpay.banking.claim.repository.ClaimEventRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.ClaimEventRegistryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClaimEventRegistryPortImpl implements ClaimEventRegistryPort {

    private final ClaimEventRepository claimEventRepository;

    @Override
    public void persistClaimEvent(Claim claim, String requestIdentifier) {
        ClaimEntity entity = ClaimEntity.from(claim);
        ClaimEvent event = ClaimEvent.builder()
                .claim(entity)
                .data(entity)
                .type(ClaimEventType.CREATE)
                .build();

        claimEventRepository.save(event);
    }
}
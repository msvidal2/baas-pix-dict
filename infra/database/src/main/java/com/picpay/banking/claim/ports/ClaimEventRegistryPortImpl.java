package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.entity.ClaimEntity;
import com.picpay.banking.claim.entity.ClaimEventEntity;
import com.picpay.banking.claim.repository.ClaimEventRepository;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimEventType;
import com.picpay.banking.pix.core.ports.claim.ClaimEventRegistryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClaimEventRegistryPortImpl implements ClaimEventRegistryPort {

    private final ClaimEventRepository claimEventRepository;

    @Override
    public void registry(String requestIdentifier, ClaimEventType eventType, Claim claim) {

        var claimEvent = ClaimEventEntity.of(
                requestIdentifier,
                claim,
                eventType);

        claimEventRepository.save(claimEvent);
    }

}
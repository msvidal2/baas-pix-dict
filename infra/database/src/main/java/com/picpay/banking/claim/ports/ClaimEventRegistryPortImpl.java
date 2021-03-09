package com.picpay.banking.claim.ports;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimEvent;
import com.picpay.banking.pix.core.ports.claim.picpay.ClaimEventRegistryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClaimEventRegistryPortImpl implements ClaimEventRegistryPort {

    // TODO criar repository da ClaimEvent

    @Override
    public void persistClaimEvent(Claim claim, String requestIdentifier, ClaimEvent claimEvent) {

        // TODO criar ClaimEvent e persistir

    }
}
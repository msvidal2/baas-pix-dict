package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.ClaimEventRegistryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ClaimEventRegistryUseCase {

    private final ClaimEventRegistryPort claimEventRegistryPort;

    public void execute(final Claim claim,
                        final String requestIdentifier) {

        claimEventRegistryPort.persistClaimEvent(claim, requestIdentifier);

    }

}
package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ClaimEventRegistryUseCase {

    public void execute(final Claim claim,
                        final String requestIdentifier,
                        final ClaimEvent claimEvent) {

        // TODO: chamar port de registro de eventos de claim

    }

}
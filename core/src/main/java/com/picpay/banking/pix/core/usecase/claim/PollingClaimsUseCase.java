package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PollingClaimsUseCase {

    private final ListClaimsBacenPort listClaimsBacenPort;

    private final SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort;

    public void execute(final Integer ispb, final Integer limit) {
        var claim = Claim.builder()
                .ispb(ispb)
                .build();

        var claimIterable = listClaimsBacenPort.list(claim, limit, null, null, null);

        claimIterable.getClaims().forEach(sendToProcessClaimNotificationPort::send);
    }

}

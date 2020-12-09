package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.FindClaimLastPollingDatePort;
import com.picpay.banking.pix.core.ports.claim.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.claim.UpdateClaimLastPollingDatePort;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PollingClaimUseCase {

    private final ListClaimsBacenPort listClaimsBacenPort;

    private final SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort;

    private final FindClaimLastPollingDatePort findClaimLastPollingDatePort;

    private final UpdateClaimLastPollingDatePort updateClaimLastPollingDatePort;

    public void execute(final Integer ispb, final Integer limit) {
        var claim = Claim.builder()
                .ispb(ispb)
                .build();

        var endDate = LocalDateTime.now(ZoneId.of("UTC"));

        var startDate = findClaimLastPollingDatePort.getDate()
                .orElse(endDate.minusHours(24));

        log.info("Start: {}, End: {}", startDate, endDate);

        List<Claim> claims = new ArrayList<>();
        ClaimIterable claimIterable;
        int countIteration = 1;

        do {
            claimIterable = listClaimsBacenPort.list(claim, limit, null, startDate, endDate);

            if(claimIterable.getClaims() != null && !claimIterable.getClaims().isEmpty()) {
                claims.addAll(claimIterable.getClaims());
            }

            if (claimIterable.getHasNext()) {
                startDate = claimIterable.getClaims().get(claimIterable.getCount() - 1).getLastModifiedDate().plusSeconds(1);
            }

            countIteration++;
        } while (claimIterable.getHasNext() && countIteration <= 25);

        log.info("Claims found: {}", claims.size());

        claims.forEach(sendToProcessClaimNotificationPort::send);

        updateClaimLastPollingDatePort.update(endDate);
    }

}

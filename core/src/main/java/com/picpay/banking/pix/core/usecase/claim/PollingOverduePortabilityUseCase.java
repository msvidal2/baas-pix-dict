package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.domain.Execution;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToProcessClaimNotificationPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.picpay.banking.pix.core.domain.ExecutionType.CLAIM_POLLING;

@Slf4j
@RequiredArgsConstructor
public class PollingOverduePortabilityUseCase {

    private final ListClaimsBacenPort listClaimsBacenPort;
    private final SendToProcessClaimNotificationPort sendToProcessClaimNotificationPort;
    private final ExecutionPort executionPort;

    public void execute(final Integer ispb, final Integer limit) {
        var claim = Claim.builder()
                .ispb(ispb)
                .build();

        var endDate = LocalDateTime.now(ZoneId.of("UTC"));
        var startDate = executionPort.lastExecution(CLAIM_POLLING)
                .orElse(Execution.builder()
                        .endTime(endDate.minusHours(24))
                        .build())
                .getEndTime();

        log.info("Start: {}, End: {}", startDate, endDate);

        try {
            var claims = poll(claim, limit, startDate, endDate);
            claims.forEach(sendToProcessClaimNotificationPort::send);
        } catch (Exception e) {
            log.error("Error while polling claims: ", e);
        }
    }

    private List<Claim> poll(final Claim claim, final Integer limit,
                             LocalDateTime startDate, final LocalDateTime endDate) {

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

        return claims;
    }

}

package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendOverduePossessionClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.picpay.banking.pix.core.domain.ClaimSituation.AWAITING_CLAIM;
import static com.picpay.banking.pix.core.domain.ClaimType.POSSESSION_CLAIM;

@Slf4j
@RequiredArgsConstructor
public class PollingOverduePossessionClaimUseCase {

    private final FindClaimToCancelPort findClaimToCancelPort;
    private final SendOverduePossessionClaimPort sendOverduePossessionClaimPort;

    public void execute(Integer ispb, Integer limit) {
        List<Claim> overdueClaims = findClaimToCancelPort.find(
                POSSESSION_CLAIM,
                List.of(AWAITING_CLAIM),
                ispb,
                LocalDateTime.now(),
                limit);

        Optional.ofNullable(overdueClaims).ifPresentOrElse(
                claims -> {
                    log.info("Overdue possession claims found: " + claims.size());
                    claims.forEach(sendOverduePossessionClaimPort::send);
                },
                () -> log.info("There are no overdue possession claims")
        );
    }

}

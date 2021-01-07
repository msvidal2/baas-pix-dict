package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendOverduePossessionClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.picpay.banking.pix.core.domain.ClaimSituation.*;
import static com.picpay.banking.pix.core.domain.ClaimType.POSSESSION_CLAIM;

@Slf4j
@RequiredArgsConstructor
public class PollingOverduePossessionClaimUseCase {

    private final FindClaimToCancelPort findClaimToCancelPort;
    private final SendOverduePossessionClaimPort sendOverduePossessionClaimPort;

    private final Integer DAYS_TO_OVERDUE = 37;

    public void executeForDonor(Integer ispb, Integer limit) {
        List<Claim> overdueClaims = findClaimToCancelPort.findClaimToCancelWhereIsDonor(
                POSSESSION_CLAIM,
                List.of(AWAITING_CLAIM),
                ispb,
                LocalDateTime.now(),
                limit);

        Optional.ofNullable(overdueClaims).ifPresentOrElse(
                claims -> {
                    log.info("Overdue possession claims to confirm found: " + claims.size());
                    claims.forEach(sendOverduePossessionClaimPort::sendToConfirm);
                },
                () -> log.info("There are no overdue possession claims to confirm")
        );
    }

    public void executeForClaimer(Integer ispb, Integer limit) {
        List<Claim> overdueClaims = findClaimToCancelPort.findClaimToCancelWhereIsClaimer(
                POSSESSION_CLAIM,
                List.of(AWAITING_CLAIM, CONFIRMED),
                ispb,
                limit,
                DAYS_TO_OVERDUE);

        Optional.ofNullable(overdueClaims).ifPresentOrElse(
                claims -> {
                    log.info("Overdue possession claims to complete found: " + claims.size());
                    claims.forEach(sendOverduePossessionClaimPort::sendToCancel);
                },
                () -> log.info("There are no overdue possession claims to complete")
        );
    }

}

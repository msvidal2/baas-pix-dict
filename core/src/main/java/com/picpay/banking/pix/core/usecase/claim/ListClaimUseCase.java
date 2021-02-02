package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import com.picpay.banking.pix.core.validators.claim.ListClaimValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.util.Objects.isNull;
import static net.logstash.logback.argument.StructuredArguments.kv;


@AllArgsConstructor
@Slf4j
public class ListClaimUseCase {

    private final ListClaimPort listClaimPort;

    public ClaimIterable execute(final Claim claim, final Boolean isPending, final Integer limit, final Boolean isClaimer,
                                 final LocalDateTime startDate, final LocalDateTime endDate, final String requestIdentifier) {

        ListClaimValidator.validate(requestIdentifier, claim, limit);

        ClaimIterable claimIterable = listClaimPort.list(claim, limit, isClaimer, isPending, startDate,
                    isNull(endDate) ? LocalDateTime.now(ZoneId.of("UTC")) : endDate);

        if (claimIterable != null)
            log.info("Claim_listed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("count", claimIterable.getCount()));

        return claimIterable;
    }
}

package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.ListPendingClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.util.Objects.isNull;
import static net.logstash.logback.argument.StructuredArguments.kv;


@AllArgsConstructor
@Slf4j
public class ListClaimUseCase {

    ListPendingClaimPort listPendingClaimPort;

    ListClaimPort listClaimPort;

    private DictItemValidator<Claim> validator;

    public ClaimIterable execute(final Claim claim, final Boolean isPending, final Integer limit, final Boolean isClaimer, final Boolean isDonor,
                                 final LocalDateTime startDate, final LocalDateTime endDate, final String requestIdentifier) {

        validator.validate(claim);

        ClaimIterable claimIterable = null;

        if (isPending) {
            claimIterable = listPendingClaimPort.list(claim, limit, requestIdentifier);
        } else {
            validateClient(isClaimer, isDonor);
            validateLimit(limit);
            claimIterable = listClaimPort.list(claim, limit, isClaimer, isDonor, startDate,
                    isNull(endDate) ? LocalDateTime.now(ZoneId.of("UTC")) : endDate, requestIdentifier);
        }

        if (claimIterable != null)
            log.info("Claim_listed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("count", claimIterable.getCount()));

        return claimIterable;
    }

    private void validateClient(final Boolean isClaim, final Boolean isDonor) {
        if(isClaim == null && isDonor == null){
            throw new IllegalArgumentException("Donor or Claim is required.");
        }

        if(isClaim != null && isDonor != null){
            throw new IllegalArgumentException("Donor or Claim is required.");
        }
    }

    private void validateLimit(Integer limit) {
        if(limit <= 0){
            throw new IllegalArgumentException("Limit greater than 0 (zero) is required.");
        }
    }
}

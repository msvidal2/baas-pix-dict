package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.ListClaimPort;
import com.picpay.banking.pix.core.ports.claim.ListPendingClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;


@AllArgsConstructor
@Slf4j
public class ListClaimUseCase {

    ListPendingClaimPort listPendingClaimPort;

    ListClaimPort listClaimPort;

    private DictItemValidator<Claim> validator;

    public ClaimIterable execute(final Claim claim, final Boolean isPending, final Integer limit, final String requestIdentifier){

        validator.validate(claim);

        ClaimIterable claimIterable = null;

        if(isPending) {
            claimIterable = listPendingClaimPort.list(claim, limit, requestIdentifier);
        } else {
            claimIterable = listClaimPort.list(claim, limit, requestIdentifier);
        }

        if (claimIterable != null)
            log.info("Claim_listed",
                    kv("requestIdentifier", requestIdentifier),
                    kv("count", claimIterable.getCount()));

        return claimIterable;
    }
}

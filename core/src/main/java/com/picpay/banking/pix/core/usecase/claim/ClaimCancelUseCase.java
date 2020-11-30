package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimCancelValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class ClaimCancelUseCase {

    private CancelClaimPort claimCancelPort;

    public Claim execute(final Claim claim,
                         final boolean canceledClaimant,
                         final ClaimCancelReason reason,
                         final String requestIdentifier) {

        ClaimCancelValidator.validate(claim, canceledClaimant, reason, requestIdentifier);

        var claimCanceled = claimCancelPort.cancel(claim, canceledClaimant, reason, requestIdentifier);

//        if (claimCanceled != null)
//            log.info("Claim_canceled"
//                    , kv("requestIdentifier", requestIdentifier)
//                    , kv("claimId", claimCanceled.getClaimId()));

        return claimCanceled;
    }
}

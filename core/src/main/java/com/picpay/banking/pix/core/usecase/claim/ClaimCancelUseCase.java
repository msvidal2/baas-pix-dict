package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindByIdPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimCancelValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class ClaimCancelUseCase {

    private final CancelClaimPort claimCancelPort;

    private final FindByIdPort findByIdPort;

    public Claim execute(final Claim claimCancel,
                         final boolean canceledClaimant,
                         final ClaimCancelReason reason,
                         final String requestIdentifier) {

        ClaimCancelValidator.validate(claimCancel, canceledClaimant, reason, requestIdentifier);

        var claim = findByIdPort.find(claimCancel.getClaimId())
                .orElseThrow(ResourceNotFoundException::new);

        if(ClaimType.POSSESSION_CLAIM.equals(claim.getClaimType())) {

        }

//        claim.getClaimType()



        var claimCanceled = claimCancelPort.cancel(claimCancel, canceledClaimant, reason, requestIdentifier);

//        if (claimCanceled != null)
//            log.info("Claim_canceled"
//                    , kv("requestIdentifier", requestIdentifier)
//                    , kv("claimId", claimCanceled.getClaimId()));

        return claimCanceled;
    }
}

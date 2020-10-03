package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.request.ClaimCancelRequestDTO;
import com.picpay.banking.jdpi.fallbacks.ClaimJDClientFallback;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.ports.claim.ClaimCancelPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ClaimCancelPortImpl implements ClaimCancelPort {

    private final static String CIRCUIT_BREAKER_NAME = "cancel-claim";

    private ClaimJDClient claimJDClient;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "cancelFallback")
    public Claim cancel(Claim claim, boolean canceledClaimant, ClaimCancelReason reason, String requestIdentifier) {
        var request = ClaimCancelRequestDTO.builder()
                .claimId(claim.getClaimId())
                .ispb(claim.getIspb())
                .canceledClaimant(canceledClaimant)
                .reason(reason.getValue())
                .build();

        var response = timeLimiterExecutor
                .execute(CIRCUIT_BREAKER_NAME, () -> claimJDClient.cancel(requestIdentifier, claim.getClaimId(), request));

        return Claim.builder()
                .claimId(response.getClaimId())
                .claimSituation(ClaimSituation.resolve(response.getClaimSituation()))
                .resolutionThresholdDate(response.getResolutionThresholdDate())
                .completionThresholdDate(response.getCompletionThresholdDate())
                .lastModifiedDate(response.getLastModifiedDate())
                .build();
    }

    public Claim cancel(Claim claim, boolean canceledClaimant, ClaimCancelReason reason, String requestIdentifier, Exception e) {
        new ClaimJDClientFallback(e).cancel(null, null, null);
        return null;
    }

}

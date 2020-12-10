package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.request.ListClaimsRequest;
import com.picpay.banking.claim.dto.response.ListClaimsResponse;
import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimsBacenPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class ListClaimsBacenPortImpl implements ListClaimsBacenPort {

    private static final String CIRCUIT_BREAKER = "list-claims-bacen";

    private final BacenClaimClient bacenClaimClient;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "listFallback")
    public ClaimIterable list(Claim claim, Integer limit, Boolean isClaimer, LocalDateTime startDate, LocalDateTime endDate) {
        ListClaimsRequest request = ListClaimsRequest.from(claim, limit, startDate, endDate);

        ListClaimsResponse response = bacenClaimClient.listClaims(request);

        return response.toClaimIterable();
    }

    public ClaimIterable listFallback(Claim claim, Integer limit, Boolean isClaimer, LocalDateTime startDate, LocalDateTime endDate, Exception e) {
        throw BacenExceptionBuilder.from(e).build();
    }

}

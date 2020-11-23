package com.picpay.banking.claim.ports.bacen;

import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.request.CreateClaimRequest;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.claim.bacen.CreateClaimBacenPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@AllArgsConstructor
public class CreateClaimPortBacenImpl implements CreateClaimBacenPort {

    private static final String CIRCUIT_BREAKER_NAME = "create-claim-bacen";

    private final BacenClaimClient bacenClaimClient;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "createClaimFallback")
    public Claim createClaim(Claim claim, String requestIdentifier) {

        var request = CreateClaimRequest.from(claim);

        var response = bacenClaimClient.createClaim(request);

        return response.toClaim();
    }

    public PixKey createClaimFallback(Claim claim, String requestIdentifier, Exception e) {
        log.error("Claim_fallback_creatingBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("claimType", claim.getClaimType()),
                kv("key", claim.getKey()),
                kv("cpfCnpf", claim.getCpfCnpj()),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        //TODO: tratar essa exception
        throw new RuntimeException(e);
    }

}

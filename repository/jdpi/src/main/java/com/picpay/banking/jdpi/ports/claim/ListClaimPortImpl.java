package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.dto.request.ListClaimRequestDTO;
import com.picpay.banking.jdpi.fallbacks.ClaimJDClientFallback;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.ListClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListClaimPortImpl implements ListClaimPort {

    private final static String CIRCUIT_BREAKER_NAME = "list-claim";

    private ClaimJDClient claimJDClient;

    private ListClaimConverter converter;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "listFallback")
    public ClaimIterable list(final Claim claim, final Integer limit, final String requestIdentifier) {

        var listClaimRequestDTO = ListClaimRequestDTO.builder()
                .ispb(claim.getIspb())
                .tpPessoaLogada(claim.getPersonType() != null ? claim.getPersonType().getValue() : null)
                .cpfCnpjLogado((claim.getCpfCnpj() != null ? Long.parseLong(claim.getCpfCnpj()) : null))
                .tpContaLogada(claim.getAccountType() != null ? claim.getAccountType().getValue() : null)
                .nrLimite((limit != null ? limit : null))
                .nrAgenciaLogada(claim.getBranchNumber())
                .nrContaLogada(claim.getAccountNumber())
                .build();

        final var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> claimJDClient.list(requestIdentifier, listClaimRequestDTO), requestIdentifier);

        return converter.convert(response);
    }

    public ClaimIterable listFallback(final Claim claim, final Integer limit, final String requestIdentifier, Exception e) {
        new ClaimJDClientFallback(e).list(null, null);
        return null;
    }

}

package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.dto.ListPendingClaimRequestDTO;
import com.picpay.banking.jdpi.fallbacks.ClaimJDClientFallback;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.ListPendingClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListPendingClaimPortImpl implements ListPendingClaimPort {

    private final static String CIRCUIT_BREAKER_NAME = "list-pending-claim";

    private ClaimJDClient claimJDClient;

    private ListClaimConverter converter;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "listPendingFallback")
    public ClaimIterable list(final Claim claim, final Integer limit, final String requestIdentifier) {

        var listClaimRequestDTO = ListPendingClaimRequestDTO.builder()
            .ispb(claim.getIspb())
            .tpPessoaLogada(claim.getPersonType() != null ? claim.getPersonType().getValue() : null)
            .cpfCnpjLogado(claim.getCpfCnpj() != null ? Long.parseLong(claim.getCpfCnpj()) : null)
            .nrAgenciaLogada(claim.getBranchNumber())
            .nrContaLogada(claim.getAccountNumber())
            .tpContaLogada(claim.getAccountType() != null ? claim.getAccountType().getValue() : null)
            .nrLimite(limit)
            .build();

        final var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> claimJDClient.listPending(requestIdentifier, listClaimRequestDTO), requestIdentifier);

        return converter.convert(response);
    }

    public ClaimIterable listPendingFallback(final Claim claim, final Integer limit, final String requestIdentifier, Exception e) {
        new ClaimJDClientFallback(e).listPending(null, null);
        return null;
    }

}

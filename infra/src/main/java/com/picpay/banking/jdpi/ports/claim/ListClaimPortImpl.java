package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.dto.request.ListClaimRequestDTO;
import com.picpay.banking.jdpi.fallbacks.JDClientExceptionFactory;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.picpay.ListClaimPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ListClaimPortImpl implements ListClaimPort {

    private final static String CIRCUIT_BREAKER_NAME = "list-claim";

    private ClaimJDClient claimJDClient;

    private ListClaimConverter converter;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "listFallback")
    public ClaimIterable list(final Claim claim, final Integer limit, final Boolean isClaim, final Boolean isDonor,
                              final LocalDateTime startDate, final LocalDateTime endDate, final String requestIdentifier) {

        var listClaimRequestDTO = ListClaimRequestDTO.builder()
                .ispb(claim.getIspb())
                .tpPessoaLogada(claim.getPersonType() != null ? claim.getPersonType().getValue() : null)
                .cpfCnpjLogado((claim.getTaxId() != null ? Long.parseLong(claim.getTaxId()) : null))
                .tpContaLogada(claim.getAccountType() != null ? claim.getAccountType().getValue() : null)
                .nrLimite((limit != null ? limit : null))
                .ehReivindicador(isClaim != null ? isClaim : null)
                .ehDoador(isDonor != null ? isDonor : null)
                .nrAgenciaLogada(claim.getBranchNumber())
                .nrContaLogada(claim.getAccountNumber())
                .dtHrModificacaoInicio(startDate)
                .dtHrModificacaoFim(endDate)
                .build();

        final var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> claimJDClient.list(requestIdentifier, listClaimRequestDTO), requestIdentifier);

        return converter.convert(response);
    }

    public ClaimIterable listFallback(final Claim claim, final Integer limit, final Boolean isClaim, final Boolean isDonor,
                                      final LocalDateTime startDate, final LocalDateTime endDate,
                                      final String requestIdentifier, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

}

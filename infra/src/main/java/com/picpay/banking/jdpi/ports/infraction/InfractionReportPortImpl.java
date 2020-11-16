package com.picpay.banking.jdpi.ports.infraction;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.dto.request.AnalyzeInfractionReportDTO;
import com.picpay.banking.jdpi.dto.request.CancelInfractionDTO;
import com.picpay.banking.jdpi.dto.request.CreateInfractionReportRequestDTO;
import com.picpay.banking.jdpi.dto.request.FilterInfractionReportDTO;
import com.picpay.banking.jdpi.dto.response.InfractionReportDTO;
import com.picpay.banking.jdpi.fallbacks.JDClientExceptionFactory;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class InfractionReportPortImpl implements InfractionReportPort {

    private final static String CIRCUIT_BREAKER_CREATE_NAME = "create-infraction";
    private final static String CIRCUIT_BREAKER_LIST_PENDING_NAME = "list-pending-infraction";
    private final static String CIRCUIT_BREAKER_FIND_NAME = "find-infraction";
    private final static String CIRCUIT_BREAKER_CANCEL_NAME = "cancel-infraction";
    private final static String CIRCUIT_BREAKER_ANALYZE_NAME = "analyze-infraction";
    private final static String CIRCUIT_BREAKER_FILTER_NAME = "filter-infraction";

    private final InfractionReportJDClient infractionJDClient;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_CREATE_NAME, fallbackMethod = "createFallback")
    public InfractionReport create(final InfractionReport infractionReport, final String requestIdentifier) {

        final var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_CREATE_NAME,
                () -> infractionJDClient.create(CreateInfractionReportRequestDTO.from(infractionReport), requestIdentifier),
                requestIdentifier);

        return response.toInfractionReport();
    }

    public InfractionReport createFallback(final InfractionReport infractionReport, final String requestIdentifier, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_LIST_PENDING_NAME, fallbackMethod = "listPendingInfractionReportFallback")
    public List<InfractionReport> listPendingInfractionReport(final Integer ispb, final Integer limit) {

        final var infractionReportDTO = timeLimiterExecutor.execute(CIRCUIT_BREAKER_LIST_PENDING_NAME,
                () -> this.infractionJDClient.listPending(ispb, limit), "listPendingInfractionReport");

        return infractionReportDTO.getInfractionReports()
                .stream()
                .map(InfractionReportDTO::toInfraction)
                .collect(Collectors.toList());
    }

    public List<InfractionReport> listPendingInfractionReportFallback(final Integer ispb, final Integer limit, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_FIND_NAME, fallbackMethod = "findFallback")
    public InfractionReport find(final String infractionReportId, final Integer ispb) {
        return timeLimiterExecutor.execute(CIRCUIT_BREAKER_FIND_NAME, () ->
                infractionJDClient.find(ispb, infractionReportId),
                infractionReportId).toInfractionReport();
    }

    public InfractionReport findFallback(final String infractionReportId, final Integer ispb, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_CANCEL_NAME, fallbackMethod = "cancelFallback")
    public InfractionReport cancel(final String infractionReportId, final Integer ispb, final String requestIdentifier) {
        var cancelInfractionDTO = new CancelInfractionDTO(infractionReportId, ispb);

        var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_CANCEL_NAME,
                () -> this.infractionJDClient.cancel(cancelInfractionDTO, requestIdentifier),
                requestIdentifier);

        return response.toInfraction();
    }

    public InfractionReport cancelFallback(final String infractionReportId, final Integer ispb, final String requestIdentifier, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_ANALYZE_NAME, fallbackMethod = "analyzeFallback")
    public InfractionReport analyze(final String infractionReportId, final Integer ispb, final InfractionAnalyze analyze,
        final String requestIdentifier) {

        var analyzeInfractionReport = AnalyzeInfractionReportDTO.builder()
            .analyzeDetails(analyze.getDetails())
            .analyzeResult(analyze.getAnalyzeResult().getValue())
            .ispb(ispb)
            .infractionReportId(infractionReportId)
            .build();

        var analyzeResponse = timeLimiterExecutor.execute(CIRCUIT_BREAKER_ANALYZE_NAME,
                () -> this.infractionJDClient.analyze(analyzeInfractionReport, requestIdentifier),
                requestIdentifier);

        return analyzeResponse.toInfraction();
    }

    public InfractionReport analyzeFallback(final String infractionReportId, final Integer ispb, final InfractionAnalyze analyze,
                                    final String requestIdentifier, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_FILTER_NAME, fallbackMethod = "filterFallback")
    public List<InfractionReport> filter(Integer isbp, Boolean isDebited, Boolean isCredited, InfractionReportSituation situation,
        LocalDateTime dateStart, LocalDateTime dateEnd, Integer limit) {

        var filter = FilterInfractionReportDTO.builder()
            .ispb(isbp)
            .ehDebitado(isDebited)
            .ehCreditado(isCredited)
            .stRelatoInfracao(situation == null ? null : situation.getValue())
            .dtHrModificacaoInicio(dateStart)
            .dtHrModificacaoFim(dateEnd)
            .incluiDetalhes(true)
            .nrLimite(limit)
        .build();

        var listInfractionReportDTO = timeLimiterExecutor.execute(CIRCUIT_BREAKER_FILTER_NAME,
                () -> this.infractionJDClient.filter(filter), "infraction-filter");

        return listInfractionReportDTO.getInfractionReports().stream().map(InfractionReportDTO::toInfraction).collect(Collectors.toList());
    }

    public List<InfractionReport> filterFallback(Integer isbp, Boolean isDebited, Boolean isCredited, InfractionReportSituation situation,
                                         LocalDateTime dateStart, LocalDateTime dateEnd, Integer limit, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

}

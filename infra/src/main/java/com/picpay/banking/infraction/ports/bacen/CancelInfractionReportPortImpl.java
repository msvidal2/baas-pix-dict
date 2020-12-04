package com.picpay.banking.infraction.ports.bacen;

import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.fallbacks.PixKeyFieldResolver;
import com.picpay.banking.infraction.client.CreateInfractionBacenClient;
import com.picpay.banking.infraction.dto.request.CancelInfractionReportRequest;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.bacen.CancelInfractionReportPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 27/11/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CancelInfractionReportPortImpl implements CancelInfractionReportPort {

    private static final String CIRCUIT_BREAKER_CREATE_NAME = "cancel-infraction-bacen";
    private final CreateInfractionBacenClient bacenClient;
    private final TimeLimiterExecutor timeLimiterExecutor;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_CREATE_NAME, fallbackMethod = "fallBack")
    public InfractionReport cancel(String infractionReportId, Integer ispb, String requestIdentifier) {
        var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER_CREATE_NAME,
                () -> bacenClient.cancel(CancelInfractionReportRequest.from(infractionReportId, ispb),
                        infractionReportId), requestIdentifier);

        return response.toDomain();
    }

    public InfractionReport fallBack(String infractionReportId, Integer ispb, String requestIdentifier, Exception e) {
        log.error("Infraction_fallback_cancelBacen -> {} {} {} {}",
                kv("requestIdentifier", requestIdentifier),
                kv("infractionReportId", infractionReportId),
                kv("ispb", ispb),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e)
                .withFieldResolver(new PixKeyFieldResolver())
                .build();
    }
}

package com.picpay.banking.infraction.ports;

import com.picpay.banking.config.TimeLimiterExecutor;
import com.picpay.banking.infraction.clients.InfractionBacenClient;
import com.picpay.banking.infraction.dto.request.AcknowledgeInfractionReportRequest;
import com.picpay.banking.pix.core.ports.infraction.bacen.InfractionAcknowledgePort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 08/12/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class InfractionAcknowledgePortImpl implements InfractionAcknowledgePort {

    private static final String CIRCUIT_BREAKER = "send-infraction-acknowledge-to-bacen";

    private final TimeLimiterExecutor timeLimiterExecutor;

    private final InfractionBacenClient infractionBacenClient;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void acknowledge(String infractionReportId, String ispb) {

        var response = timeLimiterExecutor.execute(CIRCUIT_BREAKER,
                () -> infractionBacenClient.acknowledge(infractionReportId, AcknowledgeInfractionReportRequest.from(infractionReportId, ispb)),
                null);

        log.info("Claim_acknowledgeBacen",
                kv("infractionReportId", infractionReportId),
                kv("ispb", ispb),
                kv("response", response));

    }

    public void fallback(final String infractionReportId, final String ispb, Exception e) {
        log.error(e.getMessage(), e);
        log.error("InfractionAcknowledge_fallback {} {} {}",
                kv("infractionReportId", infractionReportId),
                kv("ispb", ispb),
                kv("error", e));
    }
}

package com.picpay.banking.infraction.ports.picpay;

import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.fallbacks.PixKeyFieldResolver;
import com.picpay.banking.infraction.entity.InfractionReportEntity;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportCancelPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 30/11/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InfractionReportCancelPortImpl implements InfractionReportCancelPort {

    private static final String CIRCUIT_BREAKER_CREATE_NAME = "cancel-infraction";

    private final InfractionReportRepository infractionReportRepository;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_CREATE_NAME, fallbackMethod = "fallBack")
    public void cancel(String infractionReportId) {
        infractionReportRepository.changeSituation(infractionReportId, InfractionReportSituation.CANCELED.getValue());
    }

    public InfractionReport fallBack(String infractionReportId, Exception e) {
        log.error("Infraction_fallback_cancelBacen -> {} {}",
                kv("infractionReportId", infractionReportId),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e)
                .withFieldResolver(new PixKeyFieldResolver())
                .build();
    }

}

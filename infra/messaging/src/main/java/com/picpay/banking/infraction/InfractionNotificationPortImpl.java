package com.picpay.banking.infraction;

import com.picpay.banking.config.InfractionAlertNotificationOutputStream;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionNotificationPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.GenericMessage;
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
public class InfractionNotificationPortImpl implements InfractionNotificationPort {

    private static final String CIRCUIT_BREAKER = "send-infraction-alert-notification";

    private final InfractionAlertNotificationOutputStream infractionAlertNotificationOutputStream;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void notify(InfractionReport infractionReport) {
        infractionAlertNotificationOutputStream.sendAlertNotification().send(new GenericMessage<>(infractionReport));
        log.debug("Dispatched {} to infractionAlertNotificationOutputStream", infractionReport);
    }

    public void fallback(final InfractionReport infractionReport, Exception e) {
        log.error("InfractionNotification_fallback",
                kv("claimId", infractionReport.getInfractionReportId()),
                kv("error", e));
    }
}

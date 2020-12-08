/*
 *  baas-pix-dict 1.0 12/8/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.infraction;

import com.picpay.banking.config.AcknowledgeOutputStream;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.picpay.SendToAcknowledgePort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * @author rafael.braga
 * @version 1.0 08/12/2020
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SendToAcknowledgePortImpl implements SendToAcknowledgePort {

    private final AcknowledgeOutputStream acknowledgeOutputStream;
    private static final String CIRCUIT_BREAKER = "send-to-ack-notification";

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void send(final InfractionReport infractionReport) {
        acknowledgeOutputStream.sendInfractionForAcknowledgment().send(new GenericMessage<>(infractionReport));
        log.debug("Dispatched {} to acknowledgeOutputStream", infractionReport);
    }

    public void fallback(final InfractionReport infractionReport, Exception e) {
        log.error("SendToAckNotification_fallback",
                  kv("claimId", infractionReport.getInfractionReportId()),
                  kv("error", e));
    }

}

package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.DictAction;
import com.picpay.banking.pix.core.domain.DictEvent;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.exception.PortException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pixkey.config.PixKeyEventOutputBinding;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "picpay.dict.pixkey.notification-dispatcher", havingValue = "true")
public class PixKeyEventPortImpl implements PixKeyEventPort {

    private static final String CIRCUIT_BREAKER = "pix-key-send-event";
    private static final String SKIP_RECONCILIATION = "SKIP_RECONCILIATION";
    private final PixKeyEventOutputBinding pixKeyEventOutputBinding;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasCreated(final PixKey pixKey) {
        pixKeyWasCreated(pixKey, new HashMap<>());
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasCreatedByReconciliation(final PixKey pixKey) {
        pixKeyWasCreated(pixKey, Map.of(SKIP_RECONCILIATION, true));
    }

    private void pixKeyWasCreated(final PixKey pixKey, final Map<String, ?> headers) {
        var event = DictEvent.builder()
            .action(DictAction.ADD)
            .domain(DictEvent.Domain.KEY)
            .data(pixKey)
            .build();

        var message = MessageBuilder
            .withPayload(event)
            .copyHeaders(headers)
            .build();
        pixKeyEventOutputBinding.sendPixKeyWasChanged().send(message);
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasUpdated(final PixKey pixKey) {
        pixKeyWasUpdated(pixKey, new HashMap<>());
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasUpdatedByReconciliation(final PixKey pixKey) {
        pixKeyWasUpdated(pixKey, Map.of(SKIP_RECONCILIATION, true));
    }

    public void pixKeyWasUpdated(final PixKey pixKey, Map<String, ?> headers) {
        var event = DictEvent.builder()
            .action(DictAction.UPDATE)
            .domain(DictEvent.Domain.KEY)
            .data(pixKey)
            .build();

        var message = MessageBuilder
            .withPayload(event)
            .copyHeaders(headers)
            .build();
        pixKeyEventOutputBinding.sendPixKeyWasChanged().send(message);
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasRemoved(final PixKey pixKey) {
        pixKeyWasRemoved(pixKey, new HashMap<>());
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasRemovedByReconciliation(final PixKey pixKey) {
        pixKeyWasRemoved(pixKey, Map.of(SKIP_RECONCILIATION, true));
    }

    private void pixKeyWasRemoved(final PixKey pixKey, Map<String, ?> headers) {
        var event = DictEvent.builder()
            .action(DictAction.REMOVE)
            .domain(DictEvent.Domain.KEY)
            .data(pixKey)
            .build();

        var message = MessageBuilder
            .withPayload(event)
            .copyHeaders(headers)
            .build();
        pixKeyEventOutputBinding.sendPixKeyWasChanged().send(message);
    }

    public void fallback(final PixKey pixKey, Exception e) {
        log.error("PixKeySendEventPort_fallback: {}, {}",
            kv("pixKey", pixKey),
            kv("error", e));
        throw new PortException(e);
    }

    public void fallback(final PixKey oldPixKey, final PixKey newPixKey, Exception e) {
        log.error("PixKeySendEventPort_fallback: {}, {}, {}",
            kv("oldPixKey", oldPixKey),
            kv("newPixKey", newPixKey),
            kv("error", e));
        throw new PortException(e);
    }

    public void fallback(final PixKey pixKey, final LocalDateTime removedAt, Exception e) {
        log.error("ReconciliationEventsPort_fallback: {}, {}, {}",
            kv("pixKey", pixKey),
            kv("removedAt", removedAt),
            kv("error", e));
        throw new PortException(e);
    }

}

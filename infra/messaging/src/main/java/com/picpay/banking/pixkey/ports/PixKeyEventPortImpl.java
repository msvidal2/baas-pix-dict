package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.DictEvent;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.exception.PortException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pixkey.config.PixKeyEventOutputBinding;
import com.picpay.banking.pixkey.dto.PixKeyDTO;
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

    @Async
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
            .action(DictEvent.Action.ADD)
            .domain(DictEvent.Domain.KEY)
            .data(PixKeyDTO.pixKeyWasCreated(pixKey))
            .build();

        var message = MessageBuilder
            .withPayload(event)
            .copyHeaders(headers)
            .build();
        pixKeyEventOutputBinding.sendPixKeyWasChanged().send(message);
    }

    @Async
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasEdited(final PixKey oldPixKey, final PixKey newPixKey) {
        pixKeyWasEdited(oldPixKey, newPixKey, new HashMap<>());
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasEditedByReconciliation(final PixKey oldPixKey, final PixKey newPixKey) {
        pixKeyWasEdited(oldPixKey, newPixKey, Map.of(SKIP_RECONCILIATION, true));
    }

    public void pixKeyWasEdited(final PixKey oldPixKey, final PixKey newPixKey, Map<String, ?> headers) {
        var event = DictEvent.builder()
            .action(DictEvent.Action.EDIT)
            .domain(DictEvent.Domain.KEY)
            .data(PixKeyDTO.pixKeyWasEdited(newPixKey, oldPixKey.getCid()))
            .build();

        var message = MessageBuilder
            .withPayload(event)
            .copyHeaders(headers)
            .build();
        pixKeyEventOutputBinding.sendPixKeyWasChanged().send(message);
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasDeleted(final PixKey pixKey, final LocalDateTime removedAt) {
        pixKeyWasDeleted(pixKey, removedAt, new HashMap<>());
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void pixKeyWasDeletedByReconciliation(final PixKey pixKey, final LocalDateTime removeAt) {
        pixKeyWasDeleted(pixKey, removeAt, Map.of(SKIP_RECONCILIATION, true));
    }

    private void pixKeyWasDeleted(final PixKey pixKey, final LocalDateTime removedAt, Map<String, ?> headers) {
        var event = DictEvent.builder()
            .action(DictEvent.Action.DELETE)
            .domain(DictEvent.Domain.KEY)
            .data(PixKeyDTO.pixKeyWasDeleted(pixKey, removedAt))
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

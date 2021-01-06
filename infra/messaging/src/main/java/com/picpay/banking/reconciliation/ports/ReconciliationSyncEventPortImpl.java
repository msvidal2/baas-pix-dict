package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.ReconciliationSyncEvent;
import com.picpay.banking.pix.core.domain.ReconciliationSyncEvent.ReconciliationSyncOperation;
import com.picpay.banking.pix.core.exception.PortException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ReconciliationSyncEventPort;
import com.picpay.banking.reconciliation.config.ReconciliationSyncEventOutputBinding;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReconciliationSyncEventPortImpl implements ReconciliationSyncEventPort {

    private static final String CIRCUIT_BREAKER = "new-reconciliation-cid";
    private final ReconciliationSyncEventOutputBinding reconciliationSyncEventOutputBinding;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void eventByPixKeyCreated(final PixKey pixKey) {
        var event = ReconciliationSyncEvent.builder()
            .keyType(pixKey.getType())
            .key(pixKey.getKey())
            .newCid(pixKey.getCid())
            .happenedAt(pixKey.getUpdatedAt())
            .operation(ReconciliationSyncOperation.ADD)
            .build();
        reconciliationSyncEventOutputBinding.sendReconciliationSyncEvent().send(new GenericMessage<>(event));
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void eventByPixKeyUpdated(final Optional<PixKey> oldPixKey, final PixKey newPixKey) {
        var event = ReconciliationSyncEvent.builder()
            .keyType(newPixKey.getType())
            .key(newPixKey.getKey())
            .newCid(newPixKey.getCid())
            .oldCid(oldPixKey.map(PixKey::getCid).orElse(null))
            .happenedAt(newPixKey.getUpdatedAt())
            .operation(ReconciliationSyncOperation.UPDATE)
            .build();
        reconciliationSyncEventOutputBinding.sendReconciliationSyncEvent().send(new GenericMessage<>(event));
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void eventByPixKeyRemoved(final Optional<PixKey> pixKey, final LocalDateTime removedAt) {
        pixKey.ifPresent(oldPixKeyInDatabase -> {
            var event = ReconciliationSyncEvent.builder()
                .keyType(oldPixKeyInDatabase.getType())
                .key(oldPixKeyInDatabase.getKey())
                .oldCid(oldPixKeyInDatabase.getCid())
                .happenedAt(removedAt)
                .operation(ReconciliationSyncOperation.REMOVE)
                .build();
            reconciliationSyncEventOutputBinding.sendReconciliationSyncEvent().send(new GenericMessage<>(event));
        });
    }

    public void fallback(final PixKey pixKey, Exception e) {
        log.error("ReconciliationEventsPort_fallback: {}, {}",
            kv("key", pixKey.getKey()),
            kv("error", e));
        throw new PortException(e);
    }

    public void fallback(final Optional<PixKey> oldPixKey, final PixKey newPixKey, Exception e) {
        log.error("ReconciliationEventsPort_fallback: {}, {}, {}",
            kv("oldKey", oldPixKey.map(PixKey::getKey).orElseGet(null)),
            kv("key", newPixKey.getKey()),
            kv("error", e));
        throw new PortException(e);
    }

    public void fallback(final Optional<PixKey> pixKey, final LocalDateTime removedAt, Exception e) {
        log.error("ReconciliationEventsPort_fallback: {}, {}, {}",
            kv("key", pixKey.map(PixKey::getKey).orElseGet(null)),
            kv("removedAt", removedAt),
            kv("error", e));
        throw new PortException(e);
    }

}

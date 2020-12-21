package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.ReconciliationAction;
import com.picpay.banking.pix.core.domain.ReconciliationEvent;
import com.picpay.banking.pix.core.ports.pixkey.picpay.NotifyReconciliationMessagingPort;
import com.picpay.banking.reconciliation.config.NotifyReconciliationStream;
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
public class NotifyReconciliationMessagingPortImpl implements NotifyReconciliationMessagingPort {

    private static final String CIRCUIT_BREAKER = "notify-pix-key-change-reconciliation";
    private final NotifyReconciliationStream notifyReconciliationStream;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void notifyPixKeyCreated(final PixKey pixKey) {
        var event = ReconciliationEvent.builder()
            .cid(pixKey.getCid())
            .action(ReconciliationAction.ADDED)
            .key(pixKey.getKey())
            .eventOnBacenAt(pixKey.getUpdatedAt()) // TODO: Não tenho certeza de qual data usar
            .keyType(pixKey.getType())
            .build();
        notifyReconciliationStream.notifyPixKeyChangeReconciliation().send(new GenericMessage<>(event));
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void notifyPixKeyUpdated(final Optional<PixKey> oldPixKey, final PixKey newPixKey) {
        oldPixKey.ifPresentOrElse(oldPixKeyInDatabase -> {
            var removeEvent = ReconciliationEvent.builder()
                .cid(oldPixKeyInDatabase.getCid())
                .action(ReconciliationAction.REMOVED)
                .key(oldPixKeyInDatabase.getKey())
                .eventOnBacenAt(oldPixKeyInDatabase.getUpdatedAt()) // TODO: Não tenho certeza de qual data usar
                .keyType(oldPixKeyInDatabase.getType())
                .build();
            notifyReconciliationStream.notifyPixKeyChangeReconciliation().send(new GenericMessage<>(removeEvent));
        }, () -> {
            log.error("NotifyReconciliationMessagingPort: {}, {}"
                , kv("requestIdentifier", newPixKey.getRequestId())
                , kv("key", newPixKey.getKey()));
        });

        var event = ReconciliationEvent.builder()
            .cid(newPixKey.getCid())
            .action(ReconciliationAction.ADDED)
            .key(newPixKey.getKey())
            .eventOnBacenAt(newPixKey.getUpdatedAt()) // TODO: Não tenho certeza de qual data usar
            .keyType(newPixKey.getType())
            .build();
        notifyReconciliationStream.notifyPixKeyChangeReconciliation().send(new GenericMessage<>(event));
    }


    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    public void notifyPixKeyRemoved(final PixKey pixKey, final LocalDateTime removedAt) {
        var event = ReconciliationEvent.builder()
            .cid(pixKey.getCid())
            .action(ReconciliationAction.ADDED)
            .key(pixKey.getKey())
            .eventOnBacenAt(removedAt) // TODO: Não tenho certeza de qual data usar
            .keyType(pixKey.getType())
            .build();
        notifyReconciliationStream.notifyPixKeyChangeReconciliation().send(new GenericMessage<>(event));
    }

    public void fallback(final Optional<PixKey> pixKey, Exception e) {
        log.error("NotifyReconciliationMessaging_fallback",
            kv("pixPixKey", pixKey.toString()),
            kv("error", e));
    }

    public void fallback(final Optional<PixKey> oldPixKey, final Optional<PixKey> newPixKey, Exception e) {
        log.error("NotifyReconciliationMessaging_fallback",
            kv("oldPixKey", oldPixKey.toString()),
            kv("newPixKey", newPixKey.toString()),
            kv("error", e));
    }

}

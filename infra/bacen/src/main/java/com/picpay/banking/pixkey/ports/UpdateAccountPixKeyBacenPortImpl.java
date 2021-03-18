package com.picpay.banking.pixkey.ports;

import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.fallbacks.PixKeyFieldResolver;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.ErrorEvent;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pixkey.clients.BacenKeyClient;
import com.picpay.banking.pixkey.dto.request.UpdateEntryRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 25/11/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateAccountPixKeyBacenPortImpl implements UpdateAccountPixKeyBacenPort {

    private static final String CIRCUIT_BREAKER_NAME = "update-account-pix-key-bacen";

    private final BacenKeyClient bacenKeyClient;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public DomainEvent<PixKey> update(String requestIdentifier, PixKey pixKey, Reason reason) {

        var updateEntryRequest = UpdateEntryRequest.from(pixKey, reason);

        var updateEntryResponse = bacenKeyClient.updateAccountPixKey(updateEntryRequest, pixKey.getKey());

        return DomainEvent.<PixKey>builder()
                .eventType(EventType.PIX_KEY_UPDATED_BACEN)
                .domain(Domain.PIX_KEY)
                .source(updateEntryResponse.toDomain(pixKey, requestIdentifier))
                .requestIdentifier(requestIdentifier)
                .build();
    }

    public DomainEvent<PixKey> fallbackMethod(String requestIdentifier, PixKey pixKey, Reason reason, Exception e) {
        log.error("PixKey_fallback_updateAccountBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("pixKey", pixKey.getKey()),
                kv("reason", reason),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        var bacenException = BacenExceptionBuilder.from(e)
                .withFieldResolver(new PixKeyFieldResolver())
                .build();

        switch (bacenException.getHttpStatus()) {
            case BAD_REQUEST:
            case FORBIDDEN:
            case NOT_FOUND:
                return DomainEvent.<PixKey>builder()
                        .eventType(EventType.PIX_KEY_FAILED_BACEN)
                        .domain(Domain.PIX_KEY)
                        .source(pixKey)
                        .errorEvent(ErrorEvent.builder()
                                .code(bacenException.getHttpStatus().name())
                                .description(bacenException.getMessage())
                                .build())
                        .requestIdentifier(requestIdentifier)
                        .build();
            default:
                throw bacenException;

        }
    }
}

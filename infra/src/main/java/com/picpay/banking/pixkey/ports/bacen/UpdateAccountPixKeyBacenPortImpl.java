package com.picpay.banking.pixkey.ports.bacen;

import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.fallbacks.PixKeyFieldResolver;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
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
    public PixKey update(String requestIdentifier, PixKey pixKey, UpdateReason reason) {

        var updateEntryRequest = UpdateEntryRequest.from(pixKey, reason);

        var updateEntryResponse = bacenKeyClient.updateAccountPixKey(updateEntryRequest, pixKey.getKey());

        return updateEntryResponse.toDomain(pixKey, requestIdentifier);

    }

    public PixKey fallbackMethod(String requestIdentifier, PixKey pixKey, UpdateReason reason, Exception e) {
        log.error("PixKey_fallback_updateAccountBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("pixKey", pixKey.getKey()),
                kv("reason", reason),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e)
                .withFieldResolver(new PixKeyFieldResolver())
                .build();
    }
}

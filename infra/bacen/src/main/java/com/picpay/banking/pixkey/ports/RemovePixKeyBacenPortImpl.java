package com.picpay.banking.pixkey.ports;

import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.fallbacks.PixKeyFieldResolver;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pixkey.clients.BacenKeyClient;
import com.picpay.banking.pixkey.dto.request.RemoveEntryRequest;
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
@Component
@RequiredArgsConstructor
public class RemovePixKeyBacenPortImpl implements RemovePixKeyBacenPort {

    private static final String CIRCUIT_BREAKER_NAME = "remove-account-pix-key-bacen";

    private final BacenKeyClient bacenKeyClient;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public PixKey remove(PixKey pixKey, Reason reason) {

        var removeEntryRequest = RemoveEntryRequest.from(pixKey, reason);

        var removeEntryResponse = bacenKeyClient.removeAccountPixKey(removeEntryRequest, pixKey.getKey());

        return removeEntryResponse.toDomain();
    }

    public PixKey fallbackMethod(PixKey pixKey, Reason reason, Exception e) {
        log.error("PixKey_fallback_removeAccountBacen",
            kv("pixKey", pixKey.getKey()),
            kv("reason", reason),
            kv("exceptionMessage", e.getMessage()),
            kv("exception", e));

        throw BacenExceptionBuilder.from(e)
            .withFieldResolver(new PixKeyFieldResolver())
            .build();
    }

}

package com.picpay.banking.pixkey.ports.bacen;

import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.fallbacks.PixKeyFieldResolver;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pixkey.clients.BacenKeyClient;
import com.picpay.banking.pixkey.dto.request.CreateEntryRequest;
import com.picpay.banking.pixkey.dto.request.Reason;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreatePixKeyBacenPortImpl implements CreatePixKeyBacenPort {

    private static final String CIRCUIT_BREAKER_NAME = "create-pix-key-bacen";

    private final BacenKeyClient bacenKeyClient;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public PixKey create(String requestIdentifier, PixKey pixKey, CreateReason reason) {
        var createEntryRequest = CreateEntryRequest.from(pixKey, reason, requestIdentifier);

        var response = bacenKeyClient.createPixKey(createEntryRequest);

        return response.toDomain(requestIdentifier, Reason.resolve(reason));
    }

    public PixKey fallbackMethod(String requestIdentifier, PixKey pixKey, CreateReason reason, Exception e) {
        log.error("PixKey_fallback_creatingBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("key", pixKey.getKey()),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e)
                .withFieldResolver(new PixKeyFieldResolver())
                .build();
    }

}

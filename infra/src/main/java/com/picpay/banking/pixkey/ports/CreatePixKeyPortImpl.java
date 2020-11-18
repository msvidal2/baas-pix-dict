package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private static final String CIRCUIT_BREAKER_NAME = "create-pix-key-db";

    private final PixKeyRepository pixKeyRepository;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public PixKey createPixKey(String requestIdentifier, PixKey pixKey, CreateReason reason) {
        pixKeyRepository.save(PixKeyEntity.from(pixKey, reason));

        return pixKey;
    }

    public PixKey fallbackMethod(String requestIdentifier, PixKey pixKey, CreateReason reason, Exception e) {
        log.error("PixKey_fallback_creatingDB",
                kv("requestIdentifier", requestIdentifier),
                kv("key", pixKey.getKey()),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        // TODO: tratar errors

        throw new RuntimeException(e);
    }

}

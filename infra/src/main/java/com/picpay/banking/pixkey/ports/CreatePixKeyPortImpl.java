package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private static final String CIRCUIT_BREAKER_NAME = "create-pix-key-db";

    private final PixKeyRepository repository;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public PixKey createPixKey(PixKey pixKey, CreateReason reason) {
        repository.save(PixKeyEntity.from(pixKey, reason));

        return pixKey;
    }

    public PixKey fallbackMethod(String requestIdentifier, PixKey pixKey, CreateReason reason, Exception e) {
        log.error("PixKey_fallback_creatingDB",
                kv("requestIdentifier", requestIdentifier),
                kv("key", pixKey.getKey()),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        return pixKey;
    }

}

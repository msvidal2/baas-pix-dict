package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/11/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FindPixKeyPortImpl implements FindPixKeyPort {

    private static final String CIRCUIT_BREAKER_NAME = "find-pix-key-bacen";

    private final PixKeyRepository pixKeyRepository;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public PixKey findPixKey(String pixKey) {
        Optional<PixKeyEntity> pixKeyEntity = pixKeyRepository.findById(pixKey);
        return pixKeyEntity.orElse(null).toPixKey();
    }

    public PixKey fallbackMethod(String requestIdentifier, PixKey pixKey, String userId, Exception e) {
        log.error("PixKey_fallback_creatingBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("key", pixKey.getKey()),
                kv("userId", userId),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        // TODO: tratar errors

        throw new RuntimeException(e);
    }

}

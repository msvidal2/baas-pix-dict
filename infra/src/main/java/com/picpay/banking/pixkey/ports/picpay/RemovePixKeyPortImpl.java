package com.picpay.banking.pixkey.ports.picpay;

import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.fallbacks.PixKeyFieldResolver;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
public class RemovePixKeyPortImpl implements RemovePixKeyPort {

    private static final String CIRCUIT_BREAKER_NAME = "remove-account-pix-key";

    private final PixKeyRepository pixKeyRepository;

    @Override
    @Transactional
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public PixKey remove(String pixKey, Integer participant) {

        pixKeyRepository.deleteByIdKeyAndParticipant(pixKey, participant);

        return PixKey.builder().key(pixKey).build();

    }

    public PixKey fallbackMethod(String pixKey, Integer participant, Exception e) {
        log.error("PixKey_fallback_removeAccount",
                kv("pixKey", pixKey),
                kv("participant", participant),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e)
                .withFieldResolver(new PixKeyFieldResolver())
                .build();
    }

}

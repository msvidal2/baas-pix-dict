package com.picpay.banking.pixkey.ports.picpay;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private static final String CIRCUIT_BREAKER_NAME_FIND_BY_KEY = "FindPixKeyPortImpl_find-by-key";

    private final PixKeyRepository pixKeyRepository;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME_FIND_BY_KEY, fallbackMethod = "findPixKeyFallback")
    public Optional<PixKey> findPixKey(String pixKey) {
        return pixKeyRepository.findByIdKey(pixKey)
                .map(PixKeyEntity::toPixKey);
    }

    public Optional<PixKey> findPixKeyFallback(String pixKey, Exception e) {
        log.error("PixKey_fallback_creatingBacen",
                kv("key", pixKey),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        // TODO: tratar errors

        throw new RuntimeException(e);
    }

    @Override
    public List<PixKey> findByAccount(Integer ispb, String branch, String accountNUmber, AccountType accountType) {
        return pixKeyRepository.findByAccount(ispb, branch, accountNUmber, accountType)
                .stream()
                .map(PixKeyEntity::toPixKey)
                .collect(Collectors.toList());
    }

}

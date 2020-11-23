package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pixkey.dto.request.KeyType;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.entity.PixKeyIdEntity;
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
@Component("FindPixKeyPort")
public class FindPixKeyPortImpl implements FindPixKeyPort {

    private static final String CIRCUIT_BREAKER_NAME_FIND_BY_KEY = "FindPixKeyPortImpl_find-by-key";

    private final PixKeyRepository pixKeyRepository;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME_FIND_BY_KEY, fallbackMethod = "findPixKeyFallback")
    public PixKey findPixKey(String requestIdentifier, String pixKey, String userId) {
        Optional<PixKeyEntity> pixKeyEntity = pixKeyRepository.findByIdKey(pixKey);

        return pixKeyEntity.orElse(null).toPixKey();
    }

    public PixKey findPixKeyFallback(String requestIdentifier, String pixKey, String userId, Exception e) {
        log.error("PixKey_fallback_creatingBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("key", pixKey),
                kv("userId", userId),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        // TODO: tratar errors

        throw new RuntimeException(e);
    }

    @Override
    public List<PixKey> findByAccount(String taxId, String branch, String accountNUmber, AccountType accountType) {
        return pixKeyRepository.findByAccount(taxId, branch, accountNUmber, accountType)
                .stream()
                .map(PixKeyEntity::toPixKey)
                .collect(Collectors.toList());
    }

}

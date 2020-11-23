package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.FindPixKeyConverter;
import com.picpay.banking.jdpi.fallbacks.JDClientExceptionFactory;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class FindPixKeyPortImpl implements FindPixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "find-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private FindPixKeyConverter converter;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findPixKeyFallback")
    public PixKey findPixKey(String requestIdentifier, String pixKey, String userId) {

        final var findPixKeyResponseDTO = timeLimiterExecutor
            .execute(CIRCUIT_BREAKER_NAME,
                    () -> pixKeyJDClient.findPixKey(pixKey, userId, null, null),
                    requestIdentifier);

        return converter.convert(findPixKeyResponseDTO);
    }

    @Override
    public List<PixKey> findByAccount(String taxId, String branch, String accountNumber, AccountType accountType) {
        return null;
    }

    public PixKey findPixKeyFallback(String requestIdentifier, String pixKey, String userId, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

}

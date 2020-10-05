package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.RemovePixKeyRequestDTO;
import com.picpay.banking.jdpi.fallbacks.JDClientExceptionFactory;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RemovePixKeyPortImpl implements RemovePixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "remove-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "removeFallback")
    public PixKey remove(final String requestIdentifier, final PixKey pixKey, final RemoveReason reason) {
        final var requestDTO = RemovePixKeyRequestDTO.from(pixKey, reason);

        final var response = timeLimiterExecutor
                .execute(CIRCUIT_BREAKER_NAME,
                        () -> pixKeyJDClient.removeKey(requestIdentifier, pixKey.getKey(), requestDTO),
                        requestIdentifier);

        return response.toDomain();
    }

    public PixKey removeFallback(final String requestIdentifier, final PixKey pixKey, final RemoveReason reason, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

}

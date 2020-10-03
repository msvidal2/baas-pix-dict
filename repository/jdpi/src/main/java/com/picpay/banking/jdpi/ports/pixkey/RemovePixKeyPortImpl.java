package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.RemovePixKeyRequestDTO;
import com.picpay.banking.jdpi.fallbacks.PixKeyJDClientFallback;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.RemovePixKeyPort;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@AllArgsConstructor
public class RemovePixKeyPortImpl implements RemovePixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "remove-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private TimeLimiterRegistry timeLimiterRegistry;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "removeFallback")
    public PixKey remove(final String requestIdentifier, final PixKey pixKey, final RemoveReason reason) {
        final var requestDTO = RemovePixKeyRequestDTO.from(pixKey, reason);

        var timeLimiter = timeLimiterRegistry.timeLimiter(CIRCUIT_BREAKER_NAME);

        var completableFuture = CompletableFuture.supplyAsync(() ->
                pixKeyJDClient.removeKey(requestIdentifier, pixKey.getKey(), requestDTO));

        try {
            return timeLimiter.executeFutureSupplier(() -> completableFuture).toDomain();
        } catch (FeignException e) {
            throw e;
        } catch (Exception ex) {
            log.error("client-timeout: {}", ex.getMessage());
            throw new RuntimeException("Timeout", ex);
        }
    }

    public PixKey removeFallback(final String requestIdentifier, final PixKey pixKey, final RemoveReason reason, Exception e) {
        new PixKeyJDClientFallback(e).removeKey(null, null, null);
        return null;
    }

}

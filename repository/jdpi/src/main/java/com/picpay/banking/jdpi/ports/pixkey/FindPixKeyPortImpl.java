package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.FindPixKeyConverter;
import com.picpay.banking.jdpi.fallbacks.PixKeyJDClientFallback;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@AllArgsConstructor
public class FindPixKeyPortImpl implements FindPixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "find-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private FindPixKeyConverter converter;

    private TimeLimiterRegistry timeLimiterRegistry;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "findPixKeyFallback")
    public PixKey findPixKey(String requestIdentifier, String pixKey, String userId) {

        var timeLimiter = timeLimiterRegistry.timeLimiter(CIRCUIT_BREAKER_NAME);

        var completableFuture = CompletableFuture.supplyAsync(() ->
                pixKeyJDClient.findPixKey(pixKey, userId, null, null));

        try {
            var findPixKeyResponseDTO = timeLimiter.executeFutureSupplier(() -> completableFuture);

            return converter.convert(findPixKeyResponseDTO);
        } catch (FeignException e) {
            throw e;
        } catch (Exception ex) {
            log.error("client-timeout: {}", ex.getMessage());
            throw new RuntimeException("Timeout", ex);
        }
    }

    public PixKey findPixKeyFallback(String requestIdentifier, String pixKey, String userId, Exception e) {
        new PixKeyJDClientFallback(e).findPixKey(pixKey, userId, null, null);
        return null;
    }

}

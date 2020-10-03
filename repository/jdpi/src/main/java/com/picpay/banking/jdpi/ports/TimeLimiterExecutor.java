package com.picpay.banking.jdpi.ports;

import feign.FeignException;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Component
@AllArgsConstructor
public class TimeLimiterExecutor {

    private TimeLimiterRegistry timeLimiterRegistry;

    public <T> T execute(final String timerLimiterName, final Supplier<T> supplier) {
        final var timeLimiter = timeLimiterRegistry.timeLimiter(timerLimiterName);

        final var completableFuture = CompletableFuture.supplyAsync(supplier);

        try {
            return timeLimiter.executeFutureSupplier(() -> completableFuture);
        } catch (FeignException e) {
            throw e;
        } catch (Exception ex) {
            log.error("client-timeout: {}", ex.getMessage());
            throw new RuntimeException("Timeout", ex);
        }
    }

}

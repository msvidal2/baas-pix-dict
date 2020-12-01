package com.picpay.banking.jdpi.ports;

import feign.FeignException;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@AllArgsConstructor
public class TimeLimiterExecutor {

    private final TimeLimiterRegistry timeLimiterRegistry;

    public <T> T execute(final String timerLimiterName, final Supplier<T> supplier, final String requestIdentifier) {
        final var timeLimiter = timeLimiterRegistry.timeLimiter(timerLimiterName);

        final var completableFuture = CompletableFuture.supplyAsync(supplier);

        try {
            return timeLimiter.executeFutureSupplier(() -> completableFuture);
        } catch (FeignException e) {
            log.error("TimeLimiterExecutor_feignException",
                    kv("timerLimiterName", timerLimiterName),
                    kv("requestIdentifier", requestIdentifier),
                    kv("exception", e));

            throw e;
        } catch (TimeoutException e) {
            log.error("TimeLimiterExecutor_timeoutException",
                    kv("timeoutTime", timeLimiter.getTimeLimiterConfig().getTimeoutDuration()),
                    kv("timerLimiterName", timerLimiterName),
                    kv("requestIdentifier", requestIdentifier),
                    kv("exception", e));

            log.error("client-timeout",
                    kv("requestIdentifier", requestIdentifier));

            throw new RuntimeException("TimeoutException", e);
        } catch (Exception e) {
            log.error("TimeLimiterExecutor_exception",
                    kv("timeoutTime", timeLimiter.getTimeLimiterConfig().getTimeoutDuration()),
                    kv("timerLimiterName", timerLimiterName),
                    kv("requestIdentifier", requestIdentifier),
                    kv("exception", e));

            throw new RuntimeException(e.getMessage(), e);
        }
    }

}

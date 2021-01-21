package com.picpay.banking.config;

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

    public static final String TIMER_LIMITER_NAME = "timerLimiterName";
    public static final String TIMEOUT_TIME = "timeoutTime";
    public static final String REQUEST_IDENTIFIER = "requestIdentifier";
    public static final String EXCEPTION = "exception";
    private final TimeLimiterRegistry timeLimiterRegistry;

    public <T> T execute(final String timerLimiterName, final Supplier<T> supplier, final String requestIdentifier) {
        final var timeLimiter = timeLimiterRegistry.timeLimiter(timerLimiterName);

        final var completableFuture = CompletableFuture.supplyAsync(supplier);

        try {
            return timeLimiter.executeFutureSupplier(() -> completableFuture);
        } catch (FeignException e) {
            log.error("TimeLimiterExecutor_feignException",
                    kv(TIMER_LIMITER_NAME, timerLimiterName),
                    kv(REQUEST_IDENTIFIER, requestIdentifier),
                    kv(EXCEPTION, e));

            throw e;
        } catch (TimeoutException e) {
            log.error("TimeLimiterExecutor_timeoutException",
                    kv(TIMEOUT_TIME, timeLimiter.getTimeLimiterConfig().getTimeoutDuration()),
                    kv(TIMER_LIMITER_NAME, timerLimiterName),
                    kv(REQUEST_IDENTIFIER, requestIdentifier),
                    kv(EXCEPTION, e));

            log.error("client-timeout",
                    kv(REQUEST_IDENTIFIER, requestIdentifier));

            throw new RuntimeException("TimeoutException", e);
        } catch (Exception e) {
            log.error("TimeLimiterExecutor_exception",
                    kv(TIMEOUT_TIME, timeLimiter.getTimeLimiterConfig().getTimeoutDuration()),
                    kv(TIMER_LIMITER_NAME, timerLimiterName),
                    kv(REQUEST_IDENTIFIER, requestIdentifier),
                    kv(EXCEPTION, e));

            throw new RuntimeException(e.getMessage(), e);
        }
    }

}

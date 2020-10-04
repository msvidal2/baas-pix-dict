package com.picpay.banking.jdpi.ports;

import feign.FeignException;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@AllArgsConstructor
public class TimeLimiterExecutor {

    private TimeLimiterRegistry timeLimiterRegistry;

    public <T> T execute(final String timerLimiterName, final Supplier<T> supplier, final String requestIdentifier) {
        final var timeLimiter = timeLimiterRegistry.timeLimiter(timerLimiterName);

        final var completableFuture = CompletableFuture.supplyAsync(supplier);

        try {
            return timeLimiter.executeFutureSupplier(() -> completableFuture);
        } catch (FeignException e) {
            log.error("TimeLimiterExecutor_execute",
                    kv("timerLimiterName", timerLimiterName),
                    kv("requestIdentifier", requestIdentifier),
                    kv("exception", e));

            throw e;
        } catch (Exception ex) {
            log.error("client-timeout",
                    kv("timeoutTime", timeLimiter.getTimeLimiterConfig().getTimeoutDuration()),
                    kv("timerLimiterName", timerLimiterName),
                    kv("requestIdentifier", requestIdentifier));

            log.error("TimeLimiterExecutor_timeout",
                    kv("timeoutTime", timeLimiter.getTimeLimiterConfig().getTimeoutDuration()),
                    kv("timerLimiterName", timerLimiterName),
                    kv("requestIdentifier", requestIdentifier),
                    kv("exception", ex));

            throw new RuntimeException("Timeout", ex);
        }
    }

}

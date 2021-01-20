package com.picpay.banking.interceptors;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;
import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.TIME_BASED;

@Slf4j
@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    @Primary
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        var config = CircuitBreakerConfig.custom()
                .failureRateThreshold(30)
                .slowCallRateThreshold(50)
                .slowCallDurationThreshold(Duration.ofSeconds(80))
                .slidingWindowType(COUNT_BASED)
                .slidingWindowSize(200)
                .minimumNumberOfCalls(100)
                .permittedNumberOfCallsInHalfOpenState(10)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .recordException(this::verifyRecordException)
                .build();

        return CircuitBreakerRegistry.of(config);
    }

    public boolean verifyRecordException(Throwable e) {
        if (e instanceof FeignException) {
            return verifyHttpFeignError((FeignException) e);
        }

        return true;
    }

    public boolean verifyHttpFeignError(FeignException e) {
        if (e.status() == -1) {
            return true;
        }

        var httpStatus = HttpStatus.resolve(e.status());

        if(httpStatus == null) {
            return false;
        }

        switch (httpStatus) {
            case NOT_FOUND:
            case BAD_REQUEST:
                return false;
            default:
        }

        return true;
    }

}

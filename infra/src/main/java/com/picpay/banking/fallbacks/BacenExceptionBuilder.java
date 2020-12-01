package com.picpay.banking.fallbacks;

import com.picpay.banking.exceptions.BacenException;
import com.picpay.banking.jdpi.exception.JDClientException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

// TODO: revisar e melhorar
@Slf4j
public class BacenExceptionBuilder {

    private final Exception exception;

    private FieldResolver fieldResolver;

    public BacenExceptionBuilder(Exception exception) {
        this.exception = exception;
        this.fieldResolver = new DefaultFieldResolver();
    }

    public static BacenExceptionBuilder from(final Exception e) {
        return new BacenExceptionBuilder(e);
    }

    public BacenExceptionBuilder withFieldResolver(final FieldResolver fieldResolver) {
        this.fieldResolver = fieldResolver;
        return this;
    }

    public BacenException build() {
        if(exception instanceof FeignException) {
            return handleFeignException((FeignException) exception);
        }

        return new BacenException(INTERNAL_SERVER_ERROR.getReasonPhrase(), INTERNAL_SERVER_ERROR);
    }

    private BacenException handleFeignException(FeignException e) {
        if (e.status() == -1 || e.responseBody().isEmpty()) {
            return handleConnectionErrors(e);
        }

        return handleHttpErrors(e);
    }

    private BacenException handleConnectionErrors(FeignException e) {
        if (e.getCause() instanceof UnknownHostException) {
            log.error("client-unknownHost", kv("exception", e.getCause()));
            return new BacenException(BAD_GATEWAY.getReasonPhrase(), BAD_GATEWAY);
        }

        if (e.getCause() instanceof SocketTimeoutException) {
            log.error("client-timeout", kv("exception", e.getCause()));
            return new BacenException("Timeout", BAD_GATEWAY);
        }

        log.error("unknown-error", kv("exception", e.getCause()));
        return new BacenException(BAD_GATEWAY.getReasonPhrase(), BAD_GATEWAY);
    }

    private BacenException handleHttpErrors(FeignException e) {
        var httpStatus = HttpStatus.resolve(e.status());

        var bacenError = BacenErrorBuilder.builder()
                .withFieldResolver(fieldResolver)
                .withBody(e.responseBody().get().array())
                .build();

        return new BacenException(httpStatus.getReasonPhrase(), bacenError, httpStatus, e);
    }

}

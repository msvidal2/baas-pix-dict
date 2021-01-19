package com.picpay.banking.fallbacks;

import com.picpay.banking.exceptions.BacenException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

// TODO: revisar e melhorar
@Slf4j
public class BacenExceptionBuilder {

    public static final String EXCEPTION = "exception";
    private final Exception exceptionValue;

    private FieldResolver fieldResolver;

    public BacenExceptionBuilder(Exception exceptionValue) {
        this.exceptionValue = exceptionValue;
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
        if(exceptionValue instanceof FeignException) {
            return handleFeignException((FeignException) exceptionValue);
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
            log.error("client-unknownHost", kv(EXCEPTION, e.getCause()));
            return new BacenException(BAD_GATEWAY.getReasonPhrase(), BAD_GATEWAY);
        }

        if (e.getCause() instanceof SocketTimeoutException) {
            log.error("client-timeout", kv(EXCEPTION, e.getCause()));
            return new BacenException("Timeout", BAD_GATEWAY);
        }

        log.error("unknown-error", kv(EXCEPTION, e.getCause()));
        return new BacenException(BAD_GATEWAY.getReasonPhrase(), BAD_GATEWAY);
    }

    private BacenException handleHttpErrors(FeignException e) {
        var httpStatus = HttpStatus.resolve(e.status());
        var reasonPhrase = "";

        if(httpStatus != null) {
            reasonPhrase = httpStatus.getReasonPhrase();
        }

        var bacenError = BacenErrorBuilder.builder()
                .withFieldResolver(fieldResolver)
                .withBody(e.responseBody().orElse(ByteBuffer.wrap(new byte[] {})).array())
                .build();

        return new BacenException(reasonPhrase, bacenError, httpStatus, e);
    }

}

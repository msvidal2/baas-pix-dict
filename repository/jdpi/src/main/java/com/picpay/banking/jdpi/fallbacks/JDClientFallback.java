package com.picpay.banking.jdpi.fallbacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import com.picpay.banking.jdpi.exception.JDClientException;
import com.picpay.banking.jdpi.exception.TokenException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.*;

@Slf4j
public abstract class JDClientFallback {

    private Throwable cause;

    public JDClientFallback(Throwable cause) {
        this.cause = cause;
    }

    protected JDClientException resolveException() {
        if(cause instanceof FeignException) {
            return feignExceptionResolver((FeignException) cause);

        } else if(cause instanceof HystrixRuntimeException) {
            var hystrixCause = ((HystrixRuntimeException) cause)
                .getFallbackException()
                .getCause()
                .getCause();

            if(hystrixCause instanceof TokenException) {
                return tokenManagerExceptionResolver((TokenException) hystrixCause);
            }
        }

        return new JDClientException(cause.getMessage(), cause, null, BAD_GATEWAY);
    }

    private JDClientException feignExceptionResolver(FeignException feignException) {

        if (feignException.responseBody().isEmpty()) {
            if (cause.getCause().toString().contains("UnknownHostException")) {
                log.error("client-unknownHost: " + feignException.getMessage());

                return new JDClientException(BAD_GATEWAY.getReasonPhrase(), cause, null, BAD_GATEWAY);
            } else if (feignException.getCause() instanceof SocketTimeoutException) {
                log.error("client-timeout",
                        kv("exception", feignException.getCause()));

                return new JDClientException("Timeout", cause, null, BAD_GATEWAY);
            } else {

                log.error("unknown-error: " + feignException.getMessage());

                return new JDClientException(INTERNAL_SERVER_ERROR.getReasonPhrase(), cause, null, INTERNAL_SERVER_ERROR);
            }
        }

        var error = parseError(feignException.responseBody().get().array());

        switch (HttpStatus.resolve(feignException.status())) {
            case NOT_FOUND:
                return new JDClientException(NOT_FOUND.getReasonPhrase(), cause, error, NOT_FOUND);
            case BAD_REQUEST:
                return new JDClientException(BAD_REQUEST.getReasonPhrase(), cause, error, BAD_REQUEST);
            case CONFLICT:
                return new JDClientException("Request identifier has already been used in another transaction", cause, null, CONFLICT);
            case GATEWAY_TIMEOUT:
            case REQUEST_TIMEOUT:
                log.error("client-timeout: "+ feignException.getMessage());

                return new JDClientException(GATEWAY_TIMEOUT.getReasonPhrase(), cause, null, GATEWAY_TIMEOUT);
        }

        return new JDClientException(cause.getMessage(), cause, null, BAD_GATEWAY);
    }

    private JDClientException tokenManagerExceptionResolver(TokenException hystrixCause) {
        var tokenException = hystrixCause;

        log.error(tokenException.getMessage(), tokenException);

        return new JDClientException("Connection error", tokenException, null, BAD_GATEWAY);
    }

    protected JDErrorDTO parseError(byte[] bytes) {
        if(bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return new ObjectMapper().readValue(bytes, JDErrorDTO.class);
        } catch (IOException ioException) {
            throw new JDClientException("An error has occurred when parse the feign content error", ioException, null, INTERNAL_SERVER_ERROR);
        }
    }

}

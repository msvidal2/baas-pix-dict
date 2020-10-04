package com.picpay.banking.jdpi.fallbacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.jdpi.dto.response.JDErrorDTO;
import com.picpay.banking.jdpi.exception.JDClientException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.springframework.http.HttpStatus.*;

@Slf4j
public class JDClientExceptionFactory {

    public static JDClientException from(Exception cause) {
        if (cause.getClass().isAssignableFrom(FeignException.class)) {
            return handleFeignException((FeignException) cause);
        }
        return new JDClientException(cause.getMessage(), cause, null, BAD_GATEWAY);
    }

    private static JDClientException handleFeignException(FeignException feignException) {
        if (feignException.status() == -1 || feignException.responseBody().isEmpty()) {
            return handleConnectionErrors(feignException);
        }
        return handleJDErrors(feignException);
    }

    private static JDClientException handleConnectionErrors(FeignException feignException) {
        if (feignException.getCause().getClass().isAssignableFrom(UnknownHostException.class)) {
            log.error("client-unknownHost", kv("exception", feignException.getCause()));
            return new JDClientException(BAD_GATEWAY.getReasonPhrase(), feignException, null, BAD_GATEWAY);
        }

        if (feignException.getCause().getClass().isAssignableFrom(SocketTimeoutException.class)) {
            log.error("client-timeout", kv("exception", feignException.getCause()));
            return new JDClientException("Timeout", feignException, null, BAD_GATEWAY);
        }

        log.error("unknown-error", kv("exception", feignException.getCause()));
        return new JDClientException(INTERNAL_SERVER_ERROR.getReasonPhrase(), feignException, null, INTERNAL_SERVER_ERROR);
    }

    private static JDClientException handleJDErrors(FeignException feignException) {
        var error = parseError(feignException.responseBody().get().array());

        switch (HttpStatus.resolve(feignException.status())) {
            case NOT_FOUND:
                return new JDClientException(NOT_FOUND.getReasonPhrase(), feignException, error, NOT_FOUND);
            case BAD_REQUEST:
                return new JDClientException(BAD_REQUEST.getReasonPhrase(), feignException, error, BAD_REQUEST);
            case CONFLICT:
                return new JDClientException("Request identifier has already been used in another transaction", feignException, null, CONFLICT);
            case GATEWAY_TIMEOUT:
            case REQUEST_TIMEOUT:
                log.error("client-timeout: "+ feignException.getMessage());
                return new JDClientException(GATEWAY_TIMEOUT.getReasonPhrase(), feignException, null, GATEWAY_TIMEOUT);
        }
        return new JDClientException(feignException.getMessage(), feignException, null, BAD_GATEWAY);
    }

    private static JDErrorDTO parseError(byte[] bytes) {
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

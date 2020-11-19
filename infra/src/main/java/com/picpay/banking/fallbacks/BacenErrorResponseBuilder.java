package com.picpay.banking.fallbacks;

import com.picpay.banking.exceptions.BacenException;
import feign.FeignException;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

// TODO: revisar e melhorar
public class BacenErrorResponseBuilder {

    private Exception exception;

    private FieldResolver fieldResolver;

    public BacenErrorResponseBuilder(Exception exception) {
        this.exception = exception;
        this.fieldResolver = new DefaultFieldResolver();
    }

    public static BacenErrorResponseBuilder from(final Exception e) {
        return new BacenErrorResponseBuilder(e);
    }

    public BacenErrorResponseBuilder withFieldResolver(final FieldResolver fieldResolver) {
        this.fieldResolver = fieldResolver;
        return this;
    }

    public BacenException build() {
        if(exception instanceof FeignException) {
            return handleFeignException((FeignException) exception);
        }

        // TODO: alterar erro
        return new BacenException(INTERNAL_SERVER_ERROR.getReasonPhrase(), INTERNAL_SERVER_ERROR);
    }

    private BacenException handleFeignException(FeignException e) {
        if (e.status() == -1 || e.responseBody().isEmpty()) {
            return handleConnectionErrors(e);
        }

        return handleHttpErrors(e);
    }

    private BacenException handleConnectionErrors(FeignException e) {
        // TODO: implementar tratamento de erros de conexão
        return null;
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

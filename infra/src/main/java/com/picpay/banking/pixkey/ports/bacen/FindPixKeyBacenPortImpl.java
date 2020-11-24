package com.picpay.banking.pixkey.ports.bacen;

import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.fallbacks.PixKeyFieldResolver;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyBacenPort;
import com.picpay.banking.pixkey.clients.BacenKeyClient;
import com.picpay.banking.pixkey.dto.response.GetEntryResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/11/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FindPixKeyBacenPortImpl implements FindPixKeyBacenPort {

    private static final String CIRCUIT_BREAKER_NAME = "find-pix-key-bacen";

    private final BacenKeyClient bacenKeyClient;

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public PixKey findPixKey(String requestIdentifier, String pixKey, String userId) {
        //FIXME Remover código fixo
        GetEntryResponse getEntryResponse = bacenKeyClient.findPixKey(
                "22896431",
                "9117e65bc9a6a1ed724f2302287f7aa6a8fcff72cb44fb6a51e667e2d523e517",
                "E2289643120201014221500000000001",
                pixKey);
        //TODO Ao buscar no Bacen a informação, ela deverá ser incluída no DB?
        return getEntryResponse.toPixKey();
    }

    public PixKey fallbackMethod(String requestIdentifier, String pixKey, String userId, Exception e) {
        log.error("PixKey_fallback_creatingBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e)
                .withFieldResolver(new PixKeyFieldResolver())
                .build();
    }

}

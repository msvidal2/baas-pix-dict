package com.picpay.banking.pixkey.ports;

import com.picpay.banking.fallbacks.BacenExceptionBuilder;
import com.picpay.banking.fallbacks.PixKeyFieldResolver;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyBacenPort;
import com.picpay.banking.pixkey.clients.BacenKeyClient;
import com.picpay.banking.pixkey.dto.response.GetEntryResponse;
import com.picpay.banking.util.Encrypt;
import com.picpay.banking.util.EndToEndGenerator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/11/20
 */
@Slf4j
@Component
public class FindPixKeyBacenPortImpl implements FindPixKeyBacenPort {

    private static final String CIRCUIT_BREAKER_NAME = "find-pix-key-bacen";

    private final String participant;

    private final BacenKeyClient bacenKeyClient;

    public FindPixKeyBacenPortImpl(@Value("${picpay.ispb}") String participant,
                                   BacenKeyClient bacenKeyClient) {
        this.participant = participant;
        this.bacenKeyClient = bacenKeyClient;
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackMethod")
    public PixKey findPixKey(String requestIdentifier, String pixKey, String userId) {

        var payerId = Encrypt.sha256(participant + userId);
        var endToEndId = EndToEndGenerator.generate(participant);

        GetEntryResponse getEntryResponse = bacenKeyClient.findPixKey(participant, payerId, endToEndId, pixKey);

        return getEntryResponse.toDomain(endToEndId);
    }

    public PixKey fallbackMethod(String requestIdentifier, String pixKey, String userId, Exception e) {
        log.error("PixKey_fallback_findBacen",
                kv("requestIdentifier", requestIdentifier),
                kv("exceptionMessage", e.getMessage()),
                kv("exception", e));

        throw BacenExceptionBuilder.from(e)
                .withFieldResolver(new PixKeyFieldResolver())
                .build();
    }

}

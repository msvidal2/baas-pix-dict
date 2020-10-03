package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.CreatePixKeyConverter;
import com.picpay.banking.jdpi.dto.request.CreatePixKeyRequestDTO;
import com.picpay.banking.jdpi.fallbacks.PixKeyJDClientFallback;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "create-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private CreatePixKeyConverter converter;

    private TimeLimiterExecutor timeLimiterExecutor;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "createPixKeyFallback")
    public PixKey createPixKey(String requestIdentifier, PixKey pixKey, CreateReason reason) {
        CreatePixKeyRequestDTO requestDTO = converter.convert(pixKey, reason);

        var jdpiReturnDTO = timeLimiterExecutor
                .execute(CIRCUIT_BREAKER_NAME, () -> pixKeyJDClient.createPixKey(requestIdentifier, requestDTO));

        log.info
                ("{ \"pixKey_created\": \""+jdpiReturnDTO.getChave()
                +"\", \"NameIspb\":\""+pixKey.getNameIspb()
                +"\", \"AccountNumber\":\""+pixKey.getAccountNumber()
                +"\", \"BranchNumber\":\""+pixKey.getBranchNumber()+"\"}");

        return converter.convert(jdpiReturnDTO, pixKey);
    }

    public PixKey createPixKeyFallback(String requestIdentifier, PixKey pixKey, CreateReason reason, Exception e) {
        new PixKeyJDClientFallback(e).createPixKey(null, null);
        return null;
    }

}

package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.CreatePixKeyConverter;
import com.picpay.banking.jdpi.dto.request.CreatePixKeyRequestDTO;
import com.picpay.banking.jdpi.dto.response.CreatePixKeyResponseJDDTO;
import com.picpay.banking.jdpi.fallbacks.PixKeyJDClientFallback;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@AllArgsConstructor
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "create-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private CreatePixKeyConverter converter;

    private TimeLimiterRegistry timeLimiterRegistry;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "createPixKeyFallback")
    public PixKey createPixKey(String requestIdentifier, PixKey pixKey, CreateReason reason) {
        CreatePixKeyRequestDTO requestDTO = converter.convert(pixKey, reason);

        var timeLimiter = timeLimiterRegistry.timeLimiter(CIRCUIT_BREAKER_NAME);

        var completableFuture = CompletableFuture.supplyAsync(() ->
                pixKeyJDClient.createPixKey(requestIdentifier, requestDTO));

        try {
            var jdpiReturnDTO = timeLimiter.executeFutureSupplier(() -> completableFuture);

            log.info
                    ("{ \"pixKey_created\": \""+jdpiReturnDTO.getChave()
                    +"\", \"NameIspb\":\""+pixKey.getNameIspb()
                    +"\", \"AccountNumber\":\""+pixKey.getAccountNumber()
                    +"\", \"BranchNumber\":\""+pixKey.getBranchNumber()+"\"}");

            return converter.convert(jdpiReturnDTO, pixKey);
        } catch (FeignException e) {
            throw e;
        } catch (Exception ex) {
            log.error("client-timeout: {}", ex.getMessage());
            throw new RuntimeException("Timeout", ex);
        }
    }

    public PixKey createPixKeyFallback(String requestIdentifier, PixKey pixKey, CreateReason reason, Exception e) {
        new PixKeyJDClientFallback(e).createPixKey(null, null);
        return null;
    }

}

package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.ListPixKeyConverter;
import com.picpay.banking.jdpi.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.jdpi.fallbacks.PixKeyJDClientFallback;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.ListPixKeyPort;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Slf4j
@AllArgsConstructor
public class ListPixKeyPortImpl implements ListPixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "list-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private ListPixKeyConverter converter;

    private TimeLimiterRegistry timeLimiterRegistry;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "listPixKeyFallback")
    public Collection<PixKey> listPixKey(final String requestIdentifier, final PixKey pixKey) {

        var listPixKeyRequestDTO = ListPixKeyRequestDTO.builder()
            .ispb(pixKey.getIspb())
            .nrAgencia(pixKey.getBranchNumber())
            .tpConta(pixKey.getAccountType().getValue())
            .nrConta(pixKey.getAccountNumber())
            .tpPessoa(pixKey.getPersonType().getValue())
            .cpfCnpj(Long.valueOf(pixKey.getTaxId())).build();

        var timeLimiter = timeLimiterRegistry.timeLimiter(CIRCUIT_BREAKER_NAME);

        var completableFuture = CompletableFuture.supplyAsync(() ->
                pixKeyJDClient.listPixKey(requestIdentifier, listPixKeyRequestDTO));

        try {
            var findPixKeyResponseDTO = timeLimiter.executeFutureSupplier(() -> completableFuture);

            return converter.convert(findPixKeyResponseDTO);
        } catch (FeignException e) {
            throw e;
        } catch (Exception ex) {
            log.error("client-timeout: {}", ex.getMessage());
            throw new RuntimeException("Timeout", ex);
        }
    }

    public Collection<PixKey> listPixKey(final String requestIdentifier, final PixKey pixKey, Exception e) {
        new PixKeyJDClientFallback(e).listPixKey(null, null);
        return null;
    }

}

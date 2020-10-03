package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.UpdateAccountPixKeyRequestDTO;
import com.picpay.banking.jdpi.fallbacks.PixKeyJDClientFallback;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@AllArgsConstructor
public class UpdateAccountPixKeyPortImpl implements UpdateAccountPixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "remove-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private TimeLimiterRegistry timeLimiterRegistry;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "updateAccountFallback")
    public PixKey updateAccount(String requestIdentifier, PixKey pixKey, UpdateReason reason) {
        final var requestDTO = UpdateAccountPixKeyRequestDTO.builder()
                .key(pixKey.getKey())
                .ispb(pixKey.getIspb())
                .branchNumber(pixKey.getBranchNumber())
                .accountType(pixKey.getAccountType().getValue())
                .accountNumber(pixKey.getAccountNumber())
                .accountOpeningDate(pixKey.getAccountOpeningDate())
                .reason(reason.getValue())
                .build();

        var timeLimiter = timeLimiterRegistry.timeLimiter(CIRCUIT_BREAKER_NAME);

        var completableFuture = CompletableFuture.supplyAsync(() ->
                pixKeyJDClient.updateAccount(requestIdentifier, pixKey.getKey(), requestDTO));

        try {
            var responseDTO = timeLimiter.executeFutureSupplier(() -> completableFuture);

            return PixKey.builder()
                    .key(responseDTO.getKey())
                    .createdAt(responseDTO.getCreatedAt())
                    .startPossessionAt(responseDTO.getStartPossessionAt())
                    .build();
        } catch (FeignException e) {
            throw e;
        } catch (Exception ex) {
            log.error("client-timeout: {}", ex.getMessage());
            throw new RuntimeException("Timeout", ex);
        }
    }

    public PixKey updateAccountFallback(String requestIdentifier, PixKey pixKey, UpdateReason reason, Exception e) {
        new PixKeyJDClientFallback(e).updateAccount(null, null, null);
        return null;
    }

}

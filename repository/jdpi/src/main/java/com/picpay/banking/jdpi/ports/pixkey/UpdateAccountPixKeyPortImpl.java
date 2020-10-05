package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.request.UpdateAccountPixKeyRequestDTO;
import com.picpay.banking.jdpi.fallbacks.JDClientExceptionFactory;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class UpdateAccountPixKeyPortImpl implements UpdateAccountPixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "remove-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private TimeLimiterExecutor timeLimiterExecutor;

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

        var responseDTO = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> pixKeyJDClient.updateAccount(requestIdentifier, pixKey.getKey(), requestDTO), requestIdentifier);

        return PixKey.builder()
                .key(responseDTO.getKey())
                .createdAt(responseDTO.getCreatedAt())
                .startPossessionAt(responseDTO.getStartPossessionAt())
                .build();
    }

    public PixKey updateAccountFallback(String requestIdentifier, PixKey pixKey, UpdateReason reason, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

}

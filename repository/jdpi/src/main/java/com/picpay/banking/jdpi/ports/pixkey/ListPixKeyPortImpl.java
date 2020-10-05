package com.picpay.banking.jdpi.ports.pixkey;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.ListPixKeyConverter;
import com.picpay.banking.jdpi.dto.request.ListPixKeyRequestDTO;
import com.picpay.banking.jdpi.fallbacks.JDClientExceptionFactory;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.ListPixKeyPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
public class ListPixKeyPortImpl implements ListPixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "list-pix-key";

    private PixKeyJDClient pixKeyJDClient;

    private ListPixKeyConverter converter;

    private TimeLimiterExecutor timeLimiterExecutor;

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

        var findPixKeyResponseDTO = timeLimiterExecutor.execute(CIRCUIT_BREAKER_NAME,
                () -> pixKeyJDClient.listPixKey(requestIdentifier, listPixKeyRequestDTO),
                requestIdentifier);

        return converter.convert(findPixKeyResponseDTO);
    }

    public Collection<PixKey> listPixKeyFallback(final String requestIdentifier, final PixKey pixKey, Exception e) {
        throw JDClientExceptionFactory.from(e);
    }

}

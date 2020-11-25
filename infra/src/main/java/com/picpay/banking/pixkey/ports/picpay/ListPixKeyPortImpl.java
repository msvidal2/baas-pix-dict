package com.picpay.banking.pixkey.ports.picpay;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListPixKeyPortImpl implements ListPixKeyPort {

    private final static String CIRCUIT_BREAKER_NAME = "list-pix-key";

    private final PixKeyRepository repository;

    @Trace
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "listPixKeyFallback")
    public List<PixKey> listPixKey(String requestIdentifier, PixKey pixKey) {
        List<PixKeyEntity> keys;

        // TODO: melhorar
        if(Objects.isNull(pixKey.getBranchNumber())) {
            keys = repository.findKeys(pixKey.getTaxId(),
                    pixKey.getPersonType(),
                    pixKey.getAccountNumber(),
                    pixKey.getAccountType(),
                    pixKey.getIspb());
        } else {
            keys = repository.findKeys(pixKey.getTaxId(),
                    pixKey.getPersonType(),
                    pixKey.getBranchNumber(),
                    pixKey.getAccountNumber(),
                    pixKey.getAccountType(),
                    pixKey.getIspb());
        }

        return keys.stream()
                .map(PixKeyEntity::toPixKey)
                .collect(Collectors.toList());
    }

    public List<PixKey> listPixKeyFallback(final String requestIdentifier, final PixKey pixKey, Exception e) {
        return Collections.emptyList();
    }

}

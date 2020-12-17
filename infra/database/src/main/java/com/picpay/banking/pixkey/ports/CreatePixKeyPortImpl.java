package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.entity.PixKeyIdEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreatePixKeyPortImpl implements CreatePixKeyPort {

    private static final String CIRCUIT_BREAKER_NAME = "create-pix-key-db";

    private final PixKeyRepository repository;

    @Override
    public PixKey createPixKey(PixKey pixKey, CreateReason reason) {
        final PixKeyEntity pixKeyEntity = PixKeyEntity.from(pixKey, reason.getValue());
        repository.save(pixKeyEntity);

        return pixKey;
    }

    @Override
    public boolean exists(final String key, final String taxId) {
        final var id = PixKeyIdEntity.builder().key(key).build();
        return this.repository.existsById(id);
    }

}

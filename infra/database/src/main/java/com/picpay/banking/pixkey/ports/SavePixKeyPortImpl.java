package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SavePixKeyPortImpl implements SavePixKeyPort {

    private final PixKeyRepository repository;

    @Override
    public PixKey savePixKey(PixKey pixKey, Reason reason) {
        final PixKeyEntity pixKeyEntity = PixKeyEntity.from(pixKey, reason);
        repository.save(pixKeyEntity);

        return pixKey;
    }

}

package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.ports.pixkey.picpay.UpdateAccountPixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 25/11/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateAccountPixKeyPortImpl implements UpdateAccountPixKeyPort {

    private final PixKeyRepository pixKeyRepository;

    @Override
    public PixKey updateAccount(PixKey pixKey, UpdateReason reason) {

        var pixKeyEntity = PixKeyEntity.from(pixKey, reason);

        var entitySaved = pixKeyRepository.save(pixKeyEntity);

        return entitySaved.toPixKey();

    }

}

package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 25/11/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RemovePixKeyPortImpl implements RemovePixKeyPort {

    private final PixKeyRepository pixKeyRepository;

    @Override
    @Transactional
    public Optional<PixKey> remove(String pixKey, Integer participant) {
        var optPixKeyEntity = pixKeyRepository.findByIdKeyAndDonatedAutomaticallyFalse(pixKey);

        optPixKeyEntity.ifPresent(pixKeyEntity -> {
            pixKeyEntity.setReason(Reason.INACTIVITY);
            pixKeyRepository.save(pixKeyEntity);
        });

        return optPixKeyEntity.map(PixKeyEntity::toPixKey);
    }

    @Override
    @Transactional
    public void removeByCid(final String cid) {
        pixKeyRepository.deleteByCid(cid);
    }

}

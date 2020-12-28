package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/11/20
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FindPixKeyPortImpl implements FindPixKeyPort {

    private final PixKeyRepository pixKeyRepository;

    @Override
    public Optional<PixKey> findPixKey(String pixKey) {
        return pixKeyRepository.findByIdKey(pixKey)
                .map(PixKeyEntity::toPixKey);
    }

    @Override
    public List<PixKey> findByAccount(Integer ispb, String branch, String accountNUmber, AccountType accountType) {
        return pixKeyRepository.findByAccount(ispb, branch, accountNUmber, accountType)
                .stream()
                .map(PixKeyEntity::toPixKey)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PixKey> findPixKeyByCid(final String cid) {
        return pixKeyRepository.findByCid(cid).map(PixKeyEntity::toPixKey);
    }

}

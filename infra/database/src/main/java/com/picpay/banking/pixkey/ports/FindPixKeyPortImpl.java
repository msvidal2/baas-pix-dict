package com.picpay.banking.pixkey.ports;

import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.entity.PixKeyIdEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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
    public Optional<PixKey> findDonatedPixKey(String pixKey) {
        return pixKeyRepository.findByIdKeyAndDonatedAutomaticallyTrue(pixKey)
                .map(PixKeyEntity::toPixKey);
    }

    @Override
    public Optional<PixKey> findPixKey(String pixKey) {
        return pixKeyRepository.findByIdKeyAndDonatedAutomaticallyFalse(pixKey)
                .map(PixKeyEntity::toPixKey);
    }

    @Override
    public List<PixKey> findByAccount(Integer ispb, String branch, String accountNUmber, AccountType accountType) {
        return pixKeyRepository.findByAccountAndDonatedAutomaticallyFalse(ispb, branch, accountNUmber, accountType)
                .stream()
                .map(PixKeyEntity::toPixKey)
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<PixKey> findAllByKeyType(final KeyType keyType, Integer page, Integer size) {
        final var pageResult = this.pixKeyRepository.findAllByIdTypeAndDonatedAutomaticallyFalse(keyType, PageRequest.of(page, size));
        final List<PixKey> pixKeys = pageResult.getContent().stream().map(PixKeyEntity::toPixKey).collect(Collectors.toList());
        return Pagination.<PixKey>builder()
            .currentPage(page)
            .pageSize(size)
            .result(pixKeys)
            .totalRecords(pageResult.getTotalElements())
            .hasNext(pageResult.hasNext())
            .hasPrevious(pageResult.hasPrevious())
            .build();
    }

    @Override
    public Optional<PixKey> findByCid(final String cid) {
        return this.pixKeyRepository.findByCidAndDonatedAutomaticallyFalse(cid).map(PixKeyEntity::toPixKey);
    }

    @Override
    public boolean exists(final String key, final String taxId) {
        final var id = PixKeyIdEntity.builder().key(key).build();
        return this.pixKeyRepository.existsById(id);
    }

    @Override
    public String computeVsync(final KeyType key) {
        return this.pixKeyRepository.computeVsync(key.name());
    }

}

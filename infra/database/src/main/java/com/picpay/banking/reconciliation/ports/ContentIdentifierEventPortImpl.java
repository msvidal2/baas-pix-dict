package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import com.picpay.banking.reconciliation.entity.ContentIdentifierEventEntity;
import com.picpay.banking.reconciliation.repository.ContentIdentifierEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContentIdentifierEventPortImpl implements ContentIdentifierEventPort {

    private final ContentIdentifierEventRepository contentIdentifierEventRepository;
    private final PixKeyRepository pixKeyRepository;

    @Override
    public Set<ContentIdentifierEvent> findAllAfterLastSuccessfulVsync(final KeyType keyType, final LocalDateTime synchronizedStart) {
        return contentIdentifierEventRepository.findByKeyTypeAndEventOnBacenAtGreaterThanEqual(keyType, synchronizedStart)
            .stream().map(ContentIdentifierEventEntity::toContentIdentifierEvent).collect(Collectors.toSet());
    }

    @Override
    public Optional<PixKey> findPixKeyByContentIdentifier(final String cid) {
        return pixKeyRepository.findByCid(cid).map(PixKeyEntity::toPixKey);
    }

    @Override
    public void save(final ContentIdentifierEvent event) {
        contentIdentifierEventRepository.save(ContentIdentifierEventEntity.from(event));
    }

}

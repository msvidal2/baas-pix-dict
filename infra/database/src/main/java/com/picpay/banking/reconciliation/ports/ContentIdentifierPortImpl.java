package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.BacenCidEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.reconciliation.entity.BacenCidEventEntity;
import com.picpay.banking.reconciliation.repository.BacenCidEventRepository;
import com.picpay.banking.reconciliation.repository.ContentIdentifierEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContentIdentifierPortImpl implements ContentIdentifierPort {

    private final ContentIdentifierEventRepository contentIdentifierEventRepository;
    private final BacenCidEventRepository bacenCidEventRepository;

    @Override
    public List<String> findAllCidsAfterLastSuccessfulVsync(final KeyType keyType, final LocalDateTime synchronizedAt) {
        return contentIdentifierEventRepository.findAllCidsAfterLastSuccessfulVsync(keyType, synchronizedAt);
    }

    @Override
    public Set<BacenCidEvent> findBacenEventsAfter(final KeyType keyType, final LocalDateTime synchronizedStart) {
        return bacenCidEventRepository.findByKeyTypeAndEventOnBacenAtAfter(keyType, synchronizedStart)
            .stream().map(BacenCidEventEntity::toDomain)
            .collect(Collectors.toSet());
    }

}

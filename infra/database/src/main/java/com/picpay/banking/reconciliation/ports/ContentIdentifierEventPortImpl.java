package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.reconciliation.ContentIdentifierEvent;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierEventPort;
import com.picpay.banking.reconciliation.repository.ContentIdentifierEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.picpay.banking.reconciliation.entity.ContentIdentifierEventEntity.fromDomain;

@Component
@RequiredArgsConstructor
public class ContentIdentifierEventPortImpl implements ContentIdentifierEventPort {

    private final ContentIdentifierEventRepository contentIdentifierEventRepository;

    @Override
    public void save(ContentIdentifierEvent event) {
        contentIdentifierEventRepository.save(fromDomain(event));
    }

}

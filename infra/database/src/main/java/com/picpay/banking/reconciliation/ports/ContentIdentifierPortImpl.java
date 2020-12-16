package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.reconciliation.repository.ContentIdentifierEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContentIdentifierPortImpl implements ContentIdentifierPort {

    private final ContentIdentifierEventRepository contentIdentifierEventRepository;

    @Override
    public List<String> findAllCidsAfterLastSuccessfulVsync(final KeyType keyType, final LocalDateTime synchronizedAt) {
        return contentIdentifierEventRepository.findAllCidsAfterLastSuccessfulVsync(keyType, synchronizedAt);
    }

}

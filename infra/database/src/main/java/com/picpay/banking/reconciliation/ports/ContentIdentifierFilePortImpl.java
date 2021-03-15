package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierFilePort;
import com.picpay.banking.reconciliation.entity.ContentIdentifierFileEntity;
import com.picpay.banking.reconciliation.repository.ContentIdentifierFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContentIdentifierFilePortImpl implements ContentIdentifierFilePort {

    private final ContentIdentifierFileRepository contentIdentifierFileRepository;

    @Override
    public void saveFile(final ContentIdentifierFile contentIdentifierFile) {
        this.contentIdentifierFileRepository.save(ContentIdentifierFileEntity.from(contentIdentifierFile));
    }

}

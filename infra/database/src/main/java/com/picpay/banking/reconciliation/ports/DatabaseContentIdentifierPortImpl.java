package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.ContentIdentifierFileAction;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.reconciliation.entity.ContentIdentifierActionEntity;
import com.picpay.banking.reconciliation.entity.ContentIdentifierFileEntity;
import com.picpay.banking.reconciliation.repository.ContentIdentifierActionRepository;
import com.picpay.banking.reconciliation.repository.ContentIdentifierFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DatabaseContentIdentifierPortImpl implements DatabaseContentIdentifierPort {

    private ContentIdentifierFileRepository contentIdentifierFileRepository;
    private ContentIdentifierActionRepository contentIdentifierActionRepository;

    @Override
    public void saveFile(final ContentIdentifierFile contentIdentifierFile) {
        this.contentIdentifierFileRepository.save(ContentIdentifierFileEntity.from(contentIdentifierFile));
    }

    @Override
    public Optional<ContentIdentifierFile> findLastFileRequested(final KeyType keyType) {
        return this.contentIdentifierFileRepository.findFirstByKeyTypeAndStatusInOrderByRequestTimeDesc(keyType, List.of(
            ContentIdentifierFile.StatusContentIdentifierFile.PROCESSING, ContentIdentifierFile.StatusContentIdentifierFile.REQUESTED))
            .map(ContentIdentifierFileEntity::toDomain);
    }


    @Override
    public void saveAction(Integer idReference, PixKey key, String cid, ContentIdentifierFileAction action) {
        final var contentIdentifierActionEntity = ContentIdentifierActionEntity.builder()
            .action(action)
            .cid(cid)
            .pixKey(PixKeyEntity.from(key, CreateReason.RECONCILIATION))
            .idContentIdentifierFile(idReference)
            .build();

        this.contentIdentifierActionRepository.save(contentIdentifierActionEntity);
    }


}

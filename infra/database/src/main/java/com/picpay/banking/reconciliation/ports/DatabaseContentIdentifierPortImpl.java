package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.ContentIdentifierFileAction;
import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pixkey.entity.PixKeyEntity;
import com.picpay.banking.reconciliation.entity.ContentIdentifierActionEntity;
import com.picpay.banking.reconciliation.entity.ContentIdentifierEntity;
import com.picpay.banking.reconciliation.entity.ContentIdentifierFileEntity;
import com.picpay.banking.reconciliation.repository.ContentIdentifierActionRepository;
import com.picpay.banking.reconciliation.repository.ContentIdentifierFileRepository;
import com.picpay.banking.reconciliation.repository.ContentIdentifierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DatabaseContentIdentifierPortImpl implements DatabaseContentIdentifierPort {

    private ContentIdentifierFileRepository contentIdentifierFileRepository;
    private ContentIdentifierRepository contentIdentifierRepository;
    private ContentIdentifierActionRepository contentIdentifierActionRepository;

    @Override
    public List<ContentIdentifier> listAfterLastSuccessfulVsync(final KeyType keyType, final LocalDateTime synchronizedAt) {
        return null;
    }

    @Override
    public void save(final ContentIdentifier contentIdentifier) {
        this.contentIdentifierRepository.save(ContentIdentifierEntity.from(contentIdentifier));
    }

    @Override
    public void saveFile(final ContentIdentifierFile contentIdentifierFile) {
        this.contentIdentifierFileRepository.save(ContentIdentifierFileEntity.from(contentIdentifierFile));
    }

    @Override
    public Optional<ContentIdentifier> findByCid(final String cid) {
        return this.contentIdentifierRepository.findById(cid).map(ContentIdentifierEntity::toDomain);
    }

    @Override
    public void delete(final String cid) {
        this.contentIdentifierRepository.deleteById(cid);
    }

    @Override
    public Optional<ContentIdentifierFile> findLastFileRequested(final KeyType keyType) {
        return this.contentIdentifierFileRepository.findFirstByKeyTypeAndStatusInOrderByRequestTimeDesc(keyType, List.of(
            ContentIdentifierFile.StatusContentIdentifierFile.PROCESSING, ContentIdentifierFile.StatusContentIdentifierFile.REQUESTED))
            .map(ContentIdentifierFileEntity::toDomain);
    }

    @Override
    public List<ContentIdentifier> listAll(KeyType keyType) {
        return this.contentIdentifierRepository.findAllByKeyType(keyType)
            .stream().map(ContentIdentifierEntity::toDomain).collect(Collectors.toList());
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

    @Override
    public Optional<ContentIdentifier> findByKey(final String key) {
        return this.contentIdentifierRepository.findByKey(key).map(ContentIdentifierEntity::toDomain);
    }

}

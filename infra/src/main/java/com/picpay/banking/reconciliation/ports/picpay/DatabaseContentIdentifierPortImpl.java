package com.picpay.banking.reconciliation.ports.picpay;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import com.picpay.banking.reconciliation.entity.ContentIdentifierEntity;
import com.picpay.banking.reconciliation.entity.ContentIdentifierFileEntity;
import com.picpay.banking.reconciliation.repository.ContentIdentifierFileRepository;
import com.picpay.banking.reconciliation.repository.ContentIdentifierRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DatabaseContentIdentifierPortImpl implements DatabaseContentIdentifierPort {

    private ContentIdentifierFileRepository contentIdentifierFileRepository;
    private ContentIdentifierRepository contentIdentifierRepository;

    @Override
    public List<ContentIdentifier> listAfterLastSuccessfulVsync(final KeyType keyType, final LocalDateTime synchronizedAt) {
        return null;
    }

    @Override
    public void save(final ContentIdentifierFile contentIdentifierFile) {
       this.contentIdentifierFileRepository.save(ContentIdentifierFileEntity.from(contentIdentifierFile));
    }

    @Override
    public Optional<ContentIdentifierFile> findLastFileRequested(final KeyType keyType) {
        return this.contentIdentifierFileRepository.findFirstByKeyTypeAndStatusInOrderByRequestTimeDesc(keyType, List.of(
            ContentIdentifierFile.StatusContentIdentifierFile.PROCESSING, ContentIdentifierFile.StatusContentIdentifierFile.REQUESTED));
    }

    @Override
    public List<String> findKeysNotSyncToRemove(final KeyType keyType, final List<String> cidsInBacen) {

        var cidsToRemove = new ArrayList<String>();

        var actualPage = 0;
        var totalPages = 0;

        do {
            var page = this.contentIdentifierRepository.findAllByKeyType(keyType,PageRequest.of(actualPage, 100));

            final var cidsInDatabase = page.toList().stream().map(ContentIdentifierEntity::getCid).collect(Collectors.toList());
            cidsInDatabase.retainAll(cidsInBacen);
            cidsToRemove.addAll(cidsInDatabase);

            totalPages = page.getTotalPages();
            actualPage++;;
        }while (actualPage <= totalPages);

        return cidsToRemove;
    }

    @Override
    public List<String> findCidsNotSync(final KeyType keyType, final List<String> cidsInBacen) {

        var actualPage = 0;
        var totalPages = 0;

        do {
            var page = this.contentIdentifierRepository.findAllByKeyType(keyType,PageRequest.of(actualPage, 100));

            final var cidsInDatabase = page.toList().stream().map(ContentIdentifierEntity::getCid).collect(Collectors.toList());
            cidsInBacen.retainAll(cidsInDatabase);

            totalPages = page.getTotalPages();
            actualPage++;
        }while (actualPage <= totalPages);

        return cidsInBacen;
    }


}

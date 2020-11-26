package com.picpay.banking.reconciliation.ports.picpay;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.reconciliation.DatabaseContentIdentifierPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class DatabaseContentIdentifierPortImpl implements DatabaseContentIdentifierPort {


    @Override
    public List<ContentIdentifier> listAfterLastSuccessfulVsync(final KeyType keyType, final LocalDateTime synchronizedAt) {
        return null;
    }

    @Override
    public void save(final ContentIdentifierFile contentIdentifierFile) {

    }

    @Override
    public Optional<ContentIdentifierFile> findFileRequested(final KeyType keyType) {
        return Optional.empty();
    }

    @Override
    public List<String> findKeysNotSyncToRemove(final KeyType keyType, final List<String> cids) {
        return null;
    }

    @Override
    public List<String> findCidsNotSync(final KeyType keyType, final List<String> cidsInBacen) {
        return null;
    }

    @Override
    public Optional<PixKey> findPixKey(final String cid) {
        return Optional.empty();
    }

}

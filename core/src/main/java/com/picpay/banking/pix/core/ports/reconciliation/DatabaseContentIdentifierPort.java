package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DatabaseContentIdentifierPort {

    List<ContentIdentifier> listAfterLastSuccessfulVsync(KeyType keyType, LocalDateTime synchronizedAt);

    void save(ContentIdentifierFile contentIdentifierFile);

    Optional<ContentIdentifierFile> findFileRequested(KeyType keyType);

    Optional<ContentIdentifier> findByCid(KeyType keyType, String cid);

    List<String> findCidsNotSyncToRemove(final KeyType keyType, List<String> cids);

    List<String> findCidsNotSync(KeyType keyType, List<String> cidsInBacen);

    Optional<PixKey> findPixKey(String cid);

}

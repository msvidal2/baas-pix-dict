package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.ContentIdentifierFileAction;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DatabaseContentIdentifierPort {

    List<ContentIdentifier> listAfterLastSuccessfulVsync(KeyType keyType, LocalDateTime synchronizedAt);

    void save(ContentIdentifier contentIdentifier);

    void saveFile(ContentIdentifierFile contentIdentifierFile);

    Optional<ContentIdentifierFile> findLastFileRequested(KeyType keyType);

    List<ContentIdentifier> listAll(KeyType keyType);

    Optional<ContentIdentifier> findByCid(String cid);

    void delete(String cid);

    void saveAction(Integer idReference, PixKey key, String cid, ContentIdentifierFileAction action);

    Optional<ContentIdentifier> findByKey(String key);

}

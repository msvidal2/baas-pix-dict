package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.ContentIdentifierFile;
import com.picpay.banking.pix.core.domain.ContentIdentifierFileAction;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;

import java.util.Optional;

public interface DatabaseContentIdentifierPort {

    void saveFile(ContentIdentifierFile contentIdentifierFile);

    Optional<ContentIdentifierFile> findLastFileRequested(KeyType keyType);

    void saveAction(Integer idReference, PixKey key, String cid, ContentIdentifierFileAction action);

}

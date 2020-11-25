package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.KeyType;

import java.time.LocalDateTime;
import java.util.List;

public interface DatabaseContentIdentifierPort {

    List<ContentIdentifier> listAfterLastSuccessfulVsync(KeyType keyType, LocalDateTime synchronizedAt);

}

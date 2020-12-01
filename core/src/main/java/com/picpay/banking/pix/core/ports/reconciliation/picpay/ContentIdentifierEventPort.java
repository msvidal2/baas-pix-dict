package com.picpay.banking.pix.core.ports.reconciliation.picpay;

import com.picpay.banking.pix.core.domain.ContentIdentifierEvent;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ContentIdentifierEventPort {

    List<ContentIdentifierEvent> findAllAfterLastSuccessfulVsync(KeyType keyType, LocalDateTime synchronizedStart);

    Optional<PixKey> findPixKeyByContentIntentifier(String cid);

}

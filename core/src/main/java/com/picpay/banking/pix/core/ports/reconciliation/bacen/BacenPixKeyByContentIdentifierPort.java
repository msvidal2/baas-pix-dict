package com.picpay.banking.pix.core.ports.reconciliation.bacen;

import com.picpay.banking.pix.core.domain.PixKey;

import java.util.Optional;

public interface BacenPixKeyByContentIdentifierPort {

    Optional<PixKey> getPixKey(String cid);

}

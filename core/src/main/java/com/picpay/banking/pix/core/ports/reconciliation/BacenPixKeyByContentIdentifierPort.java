package com.picpay.banking.pix.core.ports.reconciliation;

import com.picpay.banking.pix.core.domain.ContentIdentifier;
import com.picpay.banking.pix.core.domain.PixKey;

public interface BacenPixKeyByContentIdentifierPort {

    PixKey getPixKey(ContentIdentifier contentIdentifier);

}

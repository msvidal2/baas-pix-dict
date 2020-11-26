package com.picpay.banking.reconciliation.ports.bacen;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import org.springframework.stereotype.Component;

@Component
public class BacenPixKeyByContentIdentifierPortImpl implements BacenPixKeyByContentIdentifierPort {


    @Override
    public PixKey getPixKey(final String cid) {
        return null;
    }

}

package com.picpay.banking.reconciliation.ports.bacen;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.reconciliation.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BacenPixKeyByContentIdentifierPortImpl implements BacenPixKeyByContentIdentifierPort {

    private BacenReconciliationClient bacenReconciliationClient;
    private String participant;

    public BacenPixKeyByContentIdentifierPortImpl(final BacenReconciliationClient bacenReconciliationClient, @Value("${picpay.ispb}") String participant) {
        this.bacenReconciliationClient = bacenReconciliationClient;
        this.participant = participant;
    }

    @Override
    public PixKey getPixKey(final String cid) {
        try {
            final var response = this.bacenReconciliationClient.getEntryByCid(cid, participant);
            return response.toDomain();
        }catch (FeignException.NotFound ex){
            return null;
        }
    }

}

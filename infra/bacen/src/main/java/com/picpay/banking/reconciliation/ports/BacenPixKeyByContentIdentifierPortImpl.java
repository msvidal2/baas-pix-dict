package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BacenPixKeyByContentIdentifierPortImpl implements BacenPixKeyByContentIdentifierPort {

    private BacenReconciliationClient bacenReconciliationClient;
    private String participant;

    public BacenPixKeyByContentIdentifierPortImpl(final BacenReconciliationClient bacenReconciliationClient, @Value("${picpay.ispb}") String participant) {
        this.bacenReconciliationClient = bacenReconciliationClient;
        this.participant = participant;
    }

    @Override
    public Optional<PixKey> getPixKey(final String cid) {
        try {
            final var response = this.bacenReconciliationClient.getEntryByCid(cid, participant);
            return Optional.of(response.toDomain());
        }catch (FeignException.NotFound | FeignException.Forbidden ex){
            return Optional.empty();
        }
    }

}

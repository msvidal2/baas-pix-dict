package com.picpay.banking.reconciliation.ports;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.SyncVerifierResult;
import com.picpay.banking.pix.core.domain.SyncVerifierResultType;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pixkey.dto.request.KeyTypeBacen;
import com.picpay.banking.reconciliation.clients.BacenReconciliationClient;
import com.picpay.banking.reconciliation.dto.request.CreateSyncVerificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BacenSyncVerificationsPortImpl implements BacenSyncVerificationsPort {

    private final BacenReconciliationClient bacenReconciliationClient;

    @Value("${picpay.ispb}")
    private Integer ispb;

    @Override
    public SyncVerifierResult syncVerification(final KeyType keyType, final String vsync) {
        var request = CreateSyncVerificationRequest.builder()
            .syncVerification(CreateSyncVerificationRequest.SyncVerification.builder()
                .participant(ispb)
                .keyType(KeyTypeBacen.resolve(keyType))
                .participantSyncVerifier(vsync)
                .build())
            .build();

        var response = bacenReconciliationClient.syncVerifications(request);

        return SyncVerifierResult.builder()
            .syncVerifierLastModified(response.getSyncVerification().getSyncVerifierLastModified())
            .syncVerifierResultType(SyncVerifierResultType.valueOf(response.getSyncVerification().getResult().name()))
            .build();
    }

}

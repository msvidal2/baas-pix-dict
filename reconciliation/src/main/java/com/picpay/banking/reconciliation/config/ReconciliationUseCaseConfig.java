package com.picpay.banking.reconciliation.config;

import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.PollCidFilePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierFilePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncBacenCidEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.usecase.pixkey.PixKeyEventRegistryUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.AggregateReconciliationCheckUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.FileReconciliationCheckUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.RequestSyncFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.SincronizeCIDEventsUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.SyncVerifierUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class ReconciliationUseCaseConfig {

    @Bean
    public SyncVerifierUseCase syncVerifierUseCase(
        SyncVerifierPort syncVerifierPort,
        SyncVerifierHistoricPort syncVerifierHistoricPort,
        ContentIdentifierPort contentIdentifierPort,
        BacenSyncVerificationsPort bacenSyncVerificationsPort) {

        return new SyncVerifierUseCase(syncVerifierPort, syncVerifierHistoricPort, contentIdentifierPort,
            bacenSyncVerificationsPort);
    }

    @Bean
    public AggregateReconciliationCheckUseCase aggregateReconciliationCheckUseCase(
        ContentIdentifierPort contentIdentifierPort,
        BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort,
        FindPixKeyPort findPixKeyPort,
        SyncVerifierPort syncVerifierPort,
        BacenSyncVerificationsPort bacenSyncVerificationsPort,
        SyncVerifierHistoricPort syncVerifierHistoricPort,
        ReconciliationActionPort reconciliationActionPort) {

        return new AggregateReconciliationCheckUseCase(
            contentIdentifierPort,
            bacenPixKeyByContentIdentifierPort,
            findPixKeyPort,
            syncVerifierPort,
            bacenSyncVerificationsPort,
            syncVerifierHistoricPort,
            reconciliationActionPort);
    }

    @Bean
    public FileReconciliationCheckUseCase fileReconciliationCheckUseCase(
        ContentIdentifierFilePort contentIdentifierFilePort,
        BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort,
        FindPixKeyPort findPixKeyPort,
        SyncVerifierPort syncVerifierPort,
        BacenSyncVerificationsPort bacenSyncVerificationsPort,
        SyncVerifierHistoricPort syncVerifierHistoricPort,
        RequestSyncFileUseCase requestSyncFileUseCase,
        ReconciliationLockPort reconciliationLockPort,
        ReconciliationActionPort reconciliationActionPort) {

        return new FileReconciliationCheckUseCase(
            contentIdentifierFilePort,
            bacenPixKeyByContentIdentifierPort,
            findPixKeyPort,
            syncVerifierPort,
            bacenSyncVerificationsPort,
            syncVerifierHistoricPort,
            requestSyncFileUseCase,
            reconciliationLockPort,
            reconciliationActionPort);
    }

    @Bean
    public RequestSyncFileUseCase requestSyncFileUseCase(
        final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
        final ContentIdentifierFilePort contentIdentifierFilePort,
        final PollCidFilePort pollCidFilePort,
        @Value("${picpay.dict.sync.timeoutPollingBacenFile}") Integer timeoutPeriod,
        @Value("${picpay.dict.sync.pollingBacenFilePeriod}") Integer period) {
        return new RequestSyncFileUseCase(bacenContentIdentifierEventsPort, contentIdentifierFilePort, pollCidFilePort, timeoutPeriod, period);
    }

    @Bean
    public PollCidFilePort pollCidFilePort() {
        return new PollCidFilePort();
    }

    @Bean
    public SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase(
        final SyncBacenCidEventsPort syncBacenCidEventsPort,
        final SyncVerifierPort syncVerifierPort,
        final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort) {
        return new SincronizeCIDEventsUseCase(syncBacenCidEventsPort, syncVerifierPort, bacenContentIdentifierEventsPort);
    }

    @Bean
    public ReconciliationUseCase reconciliationUseCase(final FileReconciliationCheckUseCase syncByFileUseCase,
        final SyncVerifierUseCase syncVerifierUseCase,
        final AggregateReconciliationCheckUseCase aggregateReconciliationCheckUseCase,
        final SincronizeCIDEventsUseCase sincronizeCidsEventUseCase,
        final ReconciliationLockPort reconciliationLockPort) {

        return new ReconciliationUseCase(syncByFileUseCase,
            syncVerifierUseCase,
            aggregateReconciliationCheckUseCase,
            sincronizeCidsEventUseCase,
            reconciliationLockPort);
    }

    @Bean
    public PixKeyEventRegistryUseCase pixKeyEventRegistryUseCase(PixKeyEventRegistryPort pixKeyEventRegistryPort) {
        return new PixKeyEventRegistryUseCase(pixKeyEventRegistryPort);
    }

}
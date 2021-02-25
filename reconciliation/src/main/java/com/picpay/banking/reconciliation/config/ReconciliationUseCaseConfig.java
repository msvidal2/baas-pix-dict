package com.picpay.banking.reconciliation.config;

import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.PollCidFilePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.ReconciliationLockPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncBacenCidEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricActionPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationSyncUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.ReconciliationUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.RequestSyncFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.SincronizeCIDEventsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class ReconciliationUseCaseConfig {

    private final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort;
    private final SyncVerifierHistoricActionPort syncVerifierHistoricActionPort;
    private final SavePixKeyPort savePixKeyPort;
    private final RemovePixKeyPort removePixKeyPort;
    private final FindPixKeyPort findPixKeyPort;
    private final SyncVerifierPort syncVerifierPort;
    private final SyncVerifierHistoricPort syncVerifierHistoricPort;
    private final ContentIdentifierPort contentIdentifierPort;
    private final BacenSyncVerificationsPort bacenSyncVerificationsPort;
    private final PixKeyEventPort pixKeyEventPort;
    private final SyncBacenCidEventsPort syncBacenCidEventsPort;
    private final DatabaseContentIdentifierPort databaseContentIdentifierPort;
    private final BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort;
    private final ReconciliationLockPort reconciliationLockPort;

    @Bean
    public ReconciliationSyncUseCase reconciliationSyncUseCase() {
        return new ReconciliationSyncUseCase(syncVerifierPort, syncVerifierHistoricPort, contentIdentifierPort,
            bacenSyncVerificationsPort);
    }

    @Bean
    public FailureReconciliationSyncUseCase failureReconciliationSyncUseCase() {
        return new FailureReconciliationSyncUseCase(bacenContentIdentifierEventsPort,
            findPixKeyPort, syncVerifierPort, bacenSyncVerificationsPort, syncVerifierHistoricPort,
            syncVerifierHistoricActionPort, savePixKeyPort, removePixKeyPort, pixKeyEventPort,
            syncBacenCidEventsPort);
    }

    @Bean
    public FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase(RequestSyncFileUseCase requestSyncFileUseCase) {
        return new FailureReconciliationSyncByFileUseCase(databaseContentIdentifierPort,
            bacenPixKeyByContentIdentifierPort, savePixKeyPort, findPixKeyPort, removePixKeyPort,
            pixKeyEventPort, syncVerifierPort, bacenSyncVerificationsPort, syncVerifierHistoricPort,
            requestSyncFileUseCase, reconciliationLockPort);
    }

    @Bean
    public RequestSyncFileUseCase requestSyncFileUseCase(
        final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
        final DatabaseContentIdentifierPort databaseContentIdentifierPort,
        final PollCidFilePort pollCidFilePort,
        @Value("${picpay.dict.sync.timeoutPollingBacenFile}") Integer timeoutPeriod,
        @Value("${picpay.dict.sync.pollingBacenFilePeriod}") Integer period) {
        return new RequestSyncFileUseCase(bacenContentIdentifierEventsPort, databaseContentIdentifierPort, pollCidFilePort, timeoutPeriod, period);
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
    public ReconciliationUseCase reconciliationUseCase(final FailureReconciliationSyncByFileUseCase syncByFileUseCase,
        final ReconciliationSyncUseCase reconciliationSyncUseCase,
        final FailureReconciliationSyncUseCase failureReconciliationSyncUseCase,
        final SincronizeCIDEventsUseCase sincronizeCidsEventUseCase,
        final ReconciliationLockPort reconciliationLockPort) {

        return new ReconciliationUseCase(syncByFileUseCase,
            reconciliationSyncUseCase,
            failureReconciliationSyncUseCase,
            sincronizeCidsEventUseCase,
            reconciliationLockPort);
    }

}
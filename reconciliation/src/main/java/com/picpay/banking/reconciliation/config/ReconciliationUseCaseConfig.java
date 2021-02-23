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
import com.picpay.banking.pixkey.ports.RemovePixKeyPortImpl;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

import java.util.Properties;

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
    public RemovePixKeyPort removePixKeyPort(PixKeyRepository pixKeyRepository, PlatformTransactionManager transactionManager) {
        return enableTransactionSupport(transactionManager, new RemovePixKeyPortImpl(pixKeyRepository));
    }

    @Bean
    public FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase(@Value("${picpay.ispb}") Integer participant,
                                                                                         DatabaseContentIdentifierPort databaseContentIdentifierPort,
                                                                                         BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort,
                                                                                         SavePixKeyPort createPixKeyPort,
                                                                                         RemovePixKeyPort removePixKeyPort,
                                                                                         FindPixKeyPort findPixKeyPort,
                                                                                         PixKeyEventPort pixKeyEventPort,
                                                                                         SyncVerifierPort syncVerifierPort,
                                                                                         BacenSyncVerificationsPort bacenSyncVerificationsPort,
                                                                                         RequestSyncFileUseCase requestSyncFileUseCase,
                                                                                         final SyncVerifierHistoricPort syncVerifierHistoricPort,
                                                                                         final ReconciliationLockPort reconciliationLockPort) {

        return new FailureReconciliationSyncByFileUseCase(participant,
                                                          databaseContentIdentifierPort,
                                                          bacenPixKeyByContentIdentifierPort,
                                                          createPixKeyPort,
                                                          findPixKeyPort,
                                                          removePixKeyPort,
                                                          pixKeyEventPort,
                                                          syncVerifierPort,
                                                          bacenSyncVerificationsPort,
                                                          syncVerifierHistoricPort,
                                                          requestSyncFileUseCase,
                                                          reconciliationLockPort);
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
    public SincronizeCIDEventsUseCase sincronizeCIDEventsUseCase(PlatformTransactionManager transactionManager,
                                                                 final SyncBacenCidEventsPort syncBacenCidEventsPort,
                                                                 final SyncVerifierPort syncVerifierPort,
                                                                 final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort) {
        return enableTransactionSupport(transactionManager,
                                        new SincronizeCIDEventsUseCase(syncBacenCidEventsPort, syncVerifierPort, bacenContentIdentifierEventsPort));
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

    private <T> T enableTransactionSupport(final PlatformTransactionManager transactionManager, final T useCase) {
        TransactionProxyFactoryBean proxy = new TransactionProxyFactoryBean();
        proxy.setTransactionManager(transactionManager);
        proxy.setTarget(useCase);

        Properties transactionAttributes = new Properties();
        transactionAttributes.put("*", "PROPAGATION_REQUIRED");
        proxy.setTransactionAttributes(transactionAttributes);

        proxy.afterPropertiesSet();
        return (T) proxy.getObject();
    }

}
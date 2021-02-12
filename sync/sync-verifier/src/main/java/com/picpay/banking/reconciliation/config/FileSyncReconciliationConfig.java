/*
 *  baas-pix-dict 1.0 12/02/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */


package com.picpay.banking.reconciliation.config;

import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenContentIdentifierEventsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenPixKeyByContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.BacenSyncVerificationsPort;
import com.picpay.banking.pix.core.ports.reconciliation.bacen.PollCidFilePort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.DatabaseContentIdentifierPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierHistoricPort;
import com.picpay.banking.pix.core.ports.reconciliation.picpay.SyncVerifierPort;
import com.picpay.banking.pix.core.usecase.reconciliation.FailureReconciliationSyncByFileUseCase;
import com.picpay.banking.pix.core.usecase.reconciliation.RequestSyncFileUseCase;
import com.picpay.banking.pixkey.ports.RemovePixKeyPortImpl;
import com.picpay.banking.pixkey.ports.SavePixKeyPortImpl;
import com.picpay.banking.pixkey.repository.PixKeyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

import java.util.Properties;

/**
 * @author Luis Silva
 * @version 1.0 10/12/2020
 */
@Configuration
@EnableTransactionManagement
public class FileSyncReconciliationConfig {

    @Bean
    public RemovePixKeyPort removePixKeyPort(PixKeyRepository pixKeyRepository, PlatformTransactionManager transactionManager) {
        return enableTransactionSupport(transactionManager, new RemovePixKeyPortImpl(pixKeyRepository));
    }

    @Bean
    public SavePixKeyPort createPixKeyPort(PixKeyRepository pixKeyRepository, PlatformTransactionManager transactionManager) {
        return enableTransactionSupport(transactionManager, new SavePixKeyPortImpl(pixKeyRepository));
    }

    @Bean
    public FailureReconciliationSyncByFileUseCase failureReconciliationSyncByFileUseCase(
        @Value("${picpay.ispb}") Integer participant,
        DatabaseContentIdentifierPort databaseContentIdentifierPort,
        BacenPixKeyByContentIdentifierPort bacenPixKeyByContentIdentifierPort,
        SavePixKeyPort createPixKeyPort,
        RemovePixKeyPort removePixKeyPort,
        FindPixKeyPort findPixKeyPort,
        PixKeyEventPort pixKeyEventPort,
        SyncVerifierPort syncVerifierPort,
        BacenSyncVerificationsPort bacenSyncVerificationsPort,
        RequestSyncFileUseCase requestSyncFileUseCase,
        final SyncVerifierHistoricPort syncVerifierHistoricPort) {

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
                                                          requestSyncFileUseCase
                                                    );
    }

    @Bean
    public RequestSyncFileUseCase requestSyncFileUseCase(
        final BacenContentIdentifierEventsPort bacenContentIdentifierEventsPort,
        final DatabaseContentIdentifierPort databaseContentIdentifierPort,
        final PollCidFilePort pollCidFilePort) {
        return new RequestSyncFileUseCase(bacenContentIdentifierEventsPort, databaseContentIdentifierPort, pollCidFilePort);
    }

    @Bean
    public PollCidFilePort pollCidFilePort() {
        return new PollCidFilePort();
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

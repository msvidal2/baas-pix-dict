package com.picpay.banking.pix.sync.eventsourcing;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.usecase.reconciliation.CidProviderUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CidProviderConsumer {

    private final CidProviderUseCase cidProviderUserCase;

    @StreamListener("sync-input")
    @Trace(dispatcher = true, metricName = "SendToFinancialInstitutionUseCase")
    public void onTransferRequest(Object event) {
        cidProviderUserCase.execute();
    }

}

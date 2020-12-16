package com.picpay.banking.pix.dict.cidevents.eventsourcing;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.usecase.reconciliation.ContentIdentifierEventRecordUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CidProviderConsumer {

    private ContentIdentifierEventRecordUseCase cidProviderUseCase;

    @StreamListener("sync-input")
    @Trace(dispatcher = true, metricName = "SendToFinancialInstitutionUseCase")
    public void onTransferRequest(String event) {
        System.out.println(event);
        //cidProviderUseCase.execute();
    }

}

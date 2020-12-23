package com.picpay.banking.claim.task;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.usecase.claim.CancelPortabilityPollingUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CancelPortabilityPollingTask implements ApplicationRunner {

    private final String ispb;
    private final Integer limit;
    private final CancelPortabilityPollingUseCase cancelPortabilityPollingUseCase;

    public CancelPortabilityPollingTask(@Value("${picpay.ispb}") final String ispb,
                                        @Value("${picpay.polling.claim.limit}") final Integer limit,
                                        final CancelPortabilityPollingUseCase cancelPortabilityPollingUseCase) {
        this.ispb = ispb;
        this.limit = limit;
        this.cancelPortabilityPollingUseCase = cancelPortabilityPollingUseCase;
    }

    @Trace(dispatcher = true, metricName = "CancelPortabilityReportListPolling")
    @Override
    public void run(final ApplicationArguments args) throws Exception {
        cancelPortabilityPollingUseCase.execute(ispb, limit);
    }

}

package com.picpay.banking.claim.task;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.pix.core.usecase.claim.PollingOverduePortabilityUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortabilityClaimTask implements ApplicationRunner {

    @Value("${picpay.ispb}")
    private Integer ispb;

    @Value("${picpay.polling.claim.limit}")
    private Integer limit;

    private final PollingOverduePortabilityUseCase pollingOverduePortabilityUseCase;

    @Override
    @Trace(dispatcher = true, metricName = "claimPollingTask")
    public void run(final ApplicationArguments args) throws Exception {
        pollingOverduePortabilityUseCase.execute(ispb, limit);
    }

}

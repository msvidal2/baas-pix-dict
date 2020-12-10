package com.picpay.banking.pix.dict.schedulers.claim;

import com.picpay.banking.pix.core.usecase.claim.PollingClaimsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class PollingClaimsScheduler {

    @Value("${picpay.ispb}")
    private Integer ispb;

    @Value("${picpay.polling.claim.limit}")
    private Integer limit;

    private final PollingClaimsUseCase pollingClaimsUseCase;

    @Scheduled(initialDelayString = "${picpay.polling.claim.initial-delay}",
               fixedDelayString = "${picpay.polling.claim.fixed-delay}")
    public void run() {
        pollingClaimsUseCase.execute(ispb, limit);
    }

}

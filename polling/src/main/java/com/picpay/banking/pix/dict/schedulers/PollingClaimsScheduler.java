package com.picpay.banking.pix.dict.schedulers;

import com.picpay.banking.pix.core.usecase.claim.PollingClaimUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class PollingClaimsScheduler {

    @Value("${picpay.ispb}")
    private Integer ispb;

    @Value("${picpay.pulling.claim.limit}")
    private Integer limit;

    private final PollingClaimUseCase pollingClaimUseCase;

    @Scheduled(initialDelayString = "${picpay.pulling.claim.initial-delay}",
            fixedDelayString = "${picpay.pulling.claim.fixed-delay}")
    public void run() {
        log.info("List claims polling started");

        pollingClaimUseCase.execute(ispb, limit);

        log.info("List claims polling finished");
    }

}

package com.picpay.banking.claim.schedulers;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.usecase.claim.PollingClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.PollingPortabilityCancelUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class PollingClaimsScheduler {

    @Value("${picpay.ispb}")
    private Integer ispb;

    @Value("${picpay.polling.claim.limit}")
    private Integer limit;

    @Value("${picpay.polling.claim.cancel.limit}")
    private Integer limitCancel;

    private final PollingClaimUseCase pollingClaimUseCase;

    private final PollingPortabilityCancelUseCase pollingPortabilityCancelUseCase;

    @Scheduled(initialDelayString = "${picpay.polling.claim.initial-delay}",
            fixedDelayString = "${picpay.polling.claim.fixed-delay}")
    public void run() {
        log.info("List claims polling started");

        pollingClaimUseCase.execute(ispb, limit);

        log.info("List claims polling finished");
    }

    @Scheduled(initialDelayString = "${picpay.polling.claim.cancel.initial-delay}",
            fixedDelayString = "${picpay.polling.claim.cancel.fixed-delay}")
    public void runCancel() {
        log.info("List portabilities to cancel polling started");

        List<Claim> claimsToCancel = pollingPortabilityCancelUseCase.execute(ispb, limitCancel);

        log.info("List portabilities to cancel polling finished");
    }

}

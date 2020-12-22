package com.picpay.banking.claim.schedulers;

import com.picpay.banking.pix.core.usecase.claim.PollingOverduePossessionClaimUseCase;
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
public class PollingOverduePossessionClaim {

    @Value("${picpay.ispb}")
    private Integer ispb;

    @Value("${picpay.polling.claim.limit}")
    private Integer limit;

    private final PollingOverduePossessionClaimUseCase pollingOverduePossessionClaimUseCase;

    @Scheduled(fixedDelay = 20000)
    public void run() {
        log.info("List overdue possesion claims polling started");

        pollingOverduePossessionClaimUseCase.execute(ispb, limit);

        log.info("List overdue possesion claims polling finished");
    }

}

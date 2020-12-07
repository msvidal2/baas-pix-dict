package com.picpay.banking.pix.dict.schedulers;

import com.picpay.banking.pix.core.usecase.claim.PullingClaimUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PullingClaimsScheduler {

    @Value("${picpay.ispb}")
    private Integer ispb;

    @Value("${picpay.pulling.claim.limit}")
    private Integer limit;

    private final PullingClaimUseCase pullingClaimUseCase;

    @Scheduled(initialDelayString = "${picpay.pulling.claim.initial-delay}",
            fixedDelayString = "${picpay.pulling.claim.fixed-delay}")
    public void run() {
        pullingClaimUseCase.execute(ispb, limit);
    }

}

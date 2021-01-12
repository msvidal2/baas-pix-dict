package com.picpay.banking.claim.listeners;

import com.picpay.banking.claim.config.CancelPortabilityPollingInputBinding;
import com.picpay.banking.claim.dto.ClaimDTO;
import com.picpay.banking.pix.core.usecase.claim.OverduePortabilityClaimUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@Slf4j
public class OverduePortabilityClaimListener {

    private final OverduePortabilityClaimUseCase overduePortabilityClaimUseCase;
    private final String ispb;

    public OverduePortabilityClaimListener(@Value("${picpay.ispb}") final String ispb,
                                           final OverduePortabilityClaimUseCase overduePortabilityClaimUseCase) {
        this.ispb = ispb;
        this.overduePortabilityClaimUseCase = overduePortabilityClaimUseCase;
    }

    @StreamListener(CancelPortabilityPollingInputBinding.INPUT)
    public void listen(Message<ClaimDTO> message) {
        var claim = message.getPayload();

        log.info("new_portability_to_cancel_received",
                kv("claim_id", claim.getClaimId()),
                kv("resolution_threshold_date", claim.getResolutionThresholdDate()));

        overduePortabilityClaimUseCase.cancelClaim(claim.toDomain(), ispb);
    }

}

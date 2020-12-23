package com.picpay.banking.claim.listeners;

import com.picpay.banking.claim.config.CancelPortabilityPollingInputBinding;
import com.picpay.banking.claim.dto.ClaimDTO;
import com.picpay.banking.pix.core.usecase.claim.CancelPortabilityPollingUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@Slf4j
public class CancelPortabilityPollingListener {

    private final CancelPortabilityPollingUseCase cancelPortabilityPollingUseCase;
    private final String ispb;

    public CancelPortabilityPollingListener(@Value("${picpay.ispb}") final String ispb,
                                            final CancelPortabilityPollingUseCase cancelPortabilityPollingUseCase) {
        this.ispb = ispb;
        this.cancelPortabilityPollingUseCase = cancelPortabilityPollingUseCase;
    }

    @StreamListener(CancelPortabilityPollingInputBinding.INPUT)
    public void listen(Message<ClaimDTO> message) {
        var claim = message.getPayload();

        log.info("new_portability_to_cancel_received",
                kv("claim_id", claim.getClaimId()),
                kv("resolution_threshold_date", claim.getResolutionThresholdDate()));

        cancelPortabilityPollingUseCase.cancelClaim(claim.toDomain(), ispb);
    }

}

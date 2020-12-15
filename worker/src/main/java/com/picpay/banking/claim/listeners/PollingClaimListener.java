package com.picpay.banking.claim.listeners;

import com.picpay.banking.claim.config.ClaimNotificationInputBinding;
import com.picpay.banking.claim.dto.ClaimDTO;
import com.picpay.banking.pix.core.usecase.claim.PollingClaimListenerUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@AllArgsConstructor
@Slf4j
public class PollingClaimListener {

    private final PollingClaimListenerUseCase pollingClaimListenerUseCase;

    @StreamListener(ClaimNotificationInputBinding.INPUT)
    public void claimListener(Message<ClaimDTO> message) {

        var claim = message.getPayload();

        log.info("new_claim_notification_received",
                kv("msg_id", message.getHeaders().getId()),
                kv("Headers", message.getHeaders()),
                kv("partition", message.getHeaders().get("partition")),
                kv("claimId", claim.getClaimId()));

        pollingClaimListenerUseCase.execute(claim.toDomain());
    }

}

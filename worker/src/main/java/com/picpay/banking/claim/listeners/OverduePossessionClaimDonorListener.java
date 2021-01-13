package com.picpay.banking.claim.listeners;

import com.picpay.banking.claim.config.OverduePossessionClaimDonorInputTopic;
import com.picpay.banking.claim.dto.ClaimDTO;
import com.picpay.banking.pix.core.usecase.claim.OverduePossessionClaimDonorUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@AllArgsConstructor
@Slf4j
public class OverduePossessionClaimDonorListener {

    private final OverduePossessionClaimDonorUseCase overduePossessionClaimDonorUseCase;

    @StreamListener(OverduePossessionClaimDonorInputTopic.INPUT)
    public void claimListenerWhereIsDonor(Message<ClaimDTO> message) {

        var claim = message.getPayload();

        log.info("overdue_possession_claim_for_donor_consumed",
                kv("msg_id", message.getHeaders().getId()),
                kv("Headers", message.getHeaders()),
                kv("partition", message.getHeaders().get("partition")),
                kv("claimId", claim.getClaimId()));

        overduePossessionClaimDonorUseCase.confirm(claim.toDomain(), null);
    }

}
